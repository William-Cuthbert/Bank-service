package com.project.dto.transaction;

import com.project.enums.PaymentResult;
import com.project.enums.PaymentType;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TransactionDtoResponse {
  String id;
  String sourceAccountId;
  String targetAccountId;
  String fullName;
  double amount;
  String currency;
  String initiationDate;
  String completionDate;
  String reference;
  PaymentType type;
  PaymentResult result;
}
