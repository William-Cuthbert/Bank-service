package com.project.dto.transaction;

import com.project.enums.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDtoRequest {
  @NotBlank(message = "Source account ID is required.")
  private String sourceAccountId;

  private String targetAccountId;

  @Positive(message = "Amount must be greater than zero.")
  private double amount;

  @NotBlank(message = "Currency is required.")
  private String currency;

  private String reference;

  @NotNull(message = "Payment type is required.")
  private PaymentType type;
}

