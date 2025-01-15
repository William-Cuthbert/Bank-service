package com.project.dto.transaction;

import com.project.enums.PaymentResult;
import com.project.enums.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCriteria {
    private String accountId;
    private PaymentType type;
    private PaymentResult result;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}

