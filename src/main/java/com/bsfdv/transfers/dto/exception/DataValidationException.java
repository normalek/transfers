package com.bsfdv.transfers.dto.exception;

import com.bsfdv.transfers.entity.ErrorEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DataValidationException extends BaseException {
    private static final long serialVersionUID = 5028755228755616416L;

    public DataValidationException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage(), errorEnum.getCode(), errorEnum.getType());
    }
}
