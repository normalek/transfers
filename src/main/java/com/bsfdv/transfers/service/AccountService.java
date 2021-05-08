package com.bsfdv.transfers.service;

import com.bsfdv.transfers.dto.account.AccountRsDTO;
import com.bsfdv.transfers.dto.transfer.TransferRqDTO;
import com.bsfdv.transfers.dto.transfer.TransferRsDTO;

public interface AccountService {
    TransferRsDTO transferMoney(TransferRqDTO dto);
    AccountRsDTO getAccountById(Integer id);
}
