package com.project.dto.transaction;

import com.project.enums.PaymentResult;
import com.project.enums.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCriteria {
    private String accountId;
    private PaymentType type;
    private PaymentResult result;
    private String startDate;
    private String endDate;
}
