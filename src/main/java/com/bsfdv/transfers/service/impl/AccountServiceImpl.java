package com.bsfdv.transfers.service.impl;

import com.bsfdv.transfers.dto.account.AccountRsDTO;
import com.bsfdv.transfers.dto.exception.AccountNotFoundException;
import com.bsfdv.transfers.dto.exception.AmountOverdrawnException;
import com.bsfdv.transfers.dto.transfer.TransferRqDTO;
import com.bsfdv.transfers.dto.transfer.TransferRsDTO;
import com.bsfdv.transfers.entity.Account;
import com.bsfdv.transfers.entity.ErrorEnum;
import com.bsfdv.transfers.repository.AccountRepository;
import com.bsfdv.transfers.service.AccountService;
import com.bsfdv.transfers.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransferService transferService;

    /**
     * This method will transfer provided amount of money from one account to another.
     * It supports Optimistic locking implementation.
     * @param dto - contains amount, fromAccount and toAccount
     * @return TransferRsDTO
     * @throws AmountOverdrawnException in case of insufficient funds
     * @throws AccountNotFoundException in case of either 'to' or 'from' account wasn't found
     */
    @Override
    @Transactional(readOnly = true)
    public TransferRsDTO transferMoney(TransferRqDTO dto) {
        try {
            return transferService.transferMoney(dto);
        } catch (ObjectOptimisticLockingFailureException e) {
            log.warn("Concurrent transaction of money transferring in progress. From account: {}, to account: {}",
                    dto.getFromAccount(), dto.getToAccount());
            return transferService.transferMoney(dto);
        }
    }

    /**
     * This method will fetch Account details via provided id.
     * @param id - account id
     * @return AccountRsDTO
     * @throws AccountNotFoundException in case of account wasn't found via provided id
     */
    @Override
    @Transactional(readOnly = true)
    public AccountRsDTO getAccountById(Integer id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ErrorEnum.ACCOUNT_NOT_FOUND));

        return AccountRsDTO.builder()
                .id(account.getId())
                .alias(account.getAlias())
                .balance(account.getBalance())
                .openedDate(account.getOpenedDate())
                .status(account.getStatus()).build();
    }
}
