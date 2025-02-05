package com.project.dto.transaction;

import com.project.enums.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCriteria {
    private String accountId;
    private String reference;
    private PaymentType type;
}
