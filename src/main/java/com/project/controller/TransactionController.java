package com.project.controller;

import com.project.dto.transaction.TransactionDtoRequest;
import com.project.dto.transaction.TransactionDtoResponse;
import com.project.service.mapper.TransactionMapper;
import com.project.repository.entity.Transaction;
import com.project.service.TransactionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

  private final TransactionService transactionService;
  private final TransactionMapper transactionMapper;

  public TransactionController(TransactionService transactionService, TransactionMapper transactionMapper) {
    this.transactionService = transactionService;
    this.transactionMapper = transactionMapper;
  }

  /**
   * Process a transaction (e.g., deposit, withdrawal, transfer).
   */
  @PostMapping("/process")
  public ResponseEntity<TransactionDtoResponse> processTransaction(
          @Valid @RequestBody TransactionDtoRequest request) {
    Transaction transaction = transactionMapper.toEntity(request);
    Transaction processedTransaction = transactionService.initiate(transaction.getType(),
            transaction.getSourceAccountId(), transaction.getTargetAccountId(), transaction.getAmount(),
            transaction.getReference());
    TransactionDtoResponse response = transactionMapper.toDto(processedTransaction);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * Refund an existing transaction by its ID.
   */
  @PostMapping("/refund/{transactionId}")
  public ResponseEntity<TransactionDtoResponse> refundTransaction(
          @PathVariable @NotNull String transactionId) {
    Transaction refundTransaction = transactionService.refund(transactionId);
    TransactionDtoResponse response = transactionMapper.toDto(refundTransaction);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
