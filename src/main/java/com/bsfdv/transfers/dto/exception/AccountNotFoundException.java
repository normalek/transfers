package com.bsfdv.transfers.dto.exception;

import com.bsfdv.transfers.entity.ErrorEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends BaseException {
    private static final long serialVersionUID = 8103708662841359802L;

    public AccountNotFoundException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage(), errorEnum.getCode(), errorEnum.getType());
    }
}
