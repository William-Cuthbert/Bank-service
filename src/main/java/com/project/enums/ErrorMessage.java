package com.project.enums;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    BAD_REQUEST("badRequest", "Request could not be processed"),
    ACCOUNT_NOT_FOUND("notFound", "Account not found"),
    TRANSACTION_NOT_FOUND("notFound", "Transaction not found"),
    ACCOUNT_VERIFICATION_FAILURE("Forbidden", "Account not verified");
    private final String errorName;
    private final String message;

    ErrorMessage(String errorName, String message) {
        this.errorName = errorName;
        this.message = message;
    }
}
