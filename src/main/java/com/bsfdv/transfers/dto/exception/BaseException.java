package com.bsfdv.transfers.dto.exception;

import lombok.Data;

@Data
public class BaseException extends RuntimeException {

    private final int errorCode;
    private final String type;

    public BaseException(String message, int errorCode, String type) {
        super(message);
        this.errorCode = errorCode;
        this.type = type;
    }
}
