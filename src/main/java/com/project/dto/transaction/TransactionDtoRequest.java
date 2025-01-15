package com.project.dto.transaction;

import com.project.enums.PaymentType;

import lombok.Builder;
import lombok.Value;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Value
@Builder
public class TransactionDtoRequest {
  @NotBlank(message = "Source account ID is required.")
  String sourceAccountId;

  String targetAccountId;

  @Positive(message = "Amount must be greater than zero.")
  double amount;

  @NotBlank(message = "Currency is required.")
  String currency;

  String reference;

  @NotNull(message = "Payment type is required.")
  PaymentType type;
}
