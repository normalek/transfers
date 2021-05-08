package com.bsfdv.transfers.service;

import com.bsfdv.transfers.dto.transfer.TransferRqDTO;
import com.bsfdv.transfers.dto.transfer.TransferRsDTO;

public interface TransferService {
    TransferRsDTO transferMoney(TransferRqDTO dto);
}
