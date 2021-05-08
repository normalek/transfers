package com.bsfdv.transfers.entity;

public enum ErrorEnum {
    FROM_TO_ACCOUNT_IS_SAME("From and To account can't be the same", 400, Constants.DATA_VALIDATION),
    ACCOUNT_NOT_FOUND("Account doesn't exists", 404, Constants.DATA_VALIDATION),
    AMOUNT_OVERDRAWN("Transfer amount exceeds available balance", 402, "AMOUNT_OVERDRAWN");

    private String message;
    private int code;
    private String type;

    ErrorEnum(String message, int code, String type) {
        this.message = message;
        this.code = code;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    private static class Constants {
        public static final String DATA_VALIDATION = "DATA_VALIDATION";
    }
}
