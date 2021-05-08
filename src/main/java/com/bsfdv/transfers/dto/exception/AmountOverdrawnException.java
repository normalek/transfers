package com.bsfdv.transfers.dto.exception;

import com.bsfdv.transfers.entity.ErrorEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PAYMENT_REQUIRED)
public class AmountOverdrawnException extends BaseException {
    private static final long serialVersionUID = -3000335339074321808L;

    public AmountOverdrawnException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage(), errorEnum.getCode(), errorEnum.getType());
    }
}
