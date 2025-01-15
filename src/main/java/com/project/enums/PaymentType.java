package com.project.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentType {
    DEPOSIT("Deposit"),
    WITHDRAW("Withdraw"),
    TRANSFER("Transfer"),
    REFUND("Refund");

    private final String value;

    PaymentType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}