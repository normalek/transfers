package com.bsfdv.transfers.service.impl;

import com.bsfdv.transfers.dto.exception.AccountNotFoundException;
import com.bsfdv.transfers.dto.exception.AmountOverdrawnException;
import com.bsfdv.transfers.dto.exception.DataValidationException;
import com.bsfdv.transfers.dto.transfer.TransferRqDTO;
import com.bsfdv.transfers.dto.transfer.TransferRsDTO;
import com.bsfdv.transfers.entity.Account;
import com.bsfdv.transfers.entity.ErrorEnum;
import com.bsfdv.transfers.entity.TransferStatusEnum;
import com.bsfdv.transfers.repository.AccountRepository;
import com.bsfdv.transfers.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;

    /**
     * This method will transfer provided amount of money from one account to another.
     * It'll run in a new transaction for Optimistic locking implementation.
     * @param dto - contains amount, fromAccount and toAccount
     * @return TransferRsDTO
     * @throws AmountOverdrawnException in case of insufficient funds
     * @throws AccountNotFoundException in case of either 'to' or 'from' account wasn't found
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TransferRsDTO transferMoney(TransferRqDTO dto) {

        if (dto.getFromAccount().equals(dto.getToAccount())) {
            log.error("From {} and To {} account can't be the same", dto.getFromAccount(), dto.getToAccount());
            throw new DataValidationException(ErrorEnum.FROM_TO_ACCOUNT_IS_SAME);
        }

        Account fromAccount = accountRepository.findById(dto.getFromAccount())
                .orElseThrow(() -> new AccountNotFoundException(ErrorEnum.ACCOUNT_NOT_FOUND));
        Account toAccount = accountRepository.findById(dto.getToAccount())
                .orElseThrow(() -> new AccountNotFoundException(ErrorEnum.ACCOUNT_NOT_FOUND));

        if (fromAccount.getBalance().subtract(dto.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            log.error("Transfer amount exceeds available balance for account number : {}", fromAccount.getId());
            throw new AmountOverdrawnException(ErrorEnum.AMOUNT_OVERDRAWN);
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(dto.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(dto.getAmount()));

        return TransferRsDTO.builder()
                .toAccount(dto.getToAccount())
                .fromAccount(dto.getFromAccount())
                .transactionDate(LocalDateTime.now())
                .status(TransferStatusEnum.COMPLETED)
                .amount(dto.getAmount())
                .transactionId(UUID.randomUUID()).build();
    }
}
