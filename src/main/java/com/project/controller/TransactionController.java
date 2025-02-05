package com.project.controller;

import com.project.dto.transaction.TransactionCriteria;
import com.project.dto.transaction.TransactionDtoRequest;
import com.project.dto.transaction.TransactionDtoResponse;
import com.project.service.mapper.TransactionMapper;
import com.project.repository.entity.Transaction;
import com.project.service.TransactionService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/v1/api")
public class TransactionController {

  private final TransactionService transactionService;
  private final TransactionMapper transactionMapper;

  @Autowired
  public TransactionController(TransactionService transactionService, TransactionMapper
      transactionMapper) {
    this.transactionService = transactionService;
    this.transactionMapper = transactionMapper;
  }

  /**
   * Create a new transaction (deposit, withdrawal, transfer).
   */
  @PostMapping(value = "/payment")
  @ResponseStatus(HttpStatus.CREATED)
  public TransactionDtoResponse createPayment(@Valid @RequestBody final TransactionDtoRequest request) {
    Transaction transaction = transactionMapper.toEntity(request);
    Transaction processedTransaction = transactionService.createTransaction(
        transaction.getType(),
        transaction.getSourceAccountId(),
        transaction.getTargetAccountId(),
        transaction.getAmount(),
        transaction.getReference());
    return transactionMapper.toDto(processedTransaction);
  }

  /**
   * Refund an existing transaction by its ID.
   */
  @PostMapping(value = "/refund/{transactionId}", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public TransactionDtoResponse refundPayment(@PathVariable @NotNull final String transactionId, BindingResult result) {
    Transaction refundTransaction = transactionService.refundTransaction(transactionId);
    return transactionMapper.toDto(refundTransaction);
  }

  /**
  * Retrieves transactions based on filters.
  */
  @GetMapping(value = "/transactions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public List<TransactionDtoResponse> getPayments(@Valid final TransactionCriteria criteria, BindingResult result) {
    List<Transaction> transactions = transactionService.findTransactionsWithFilters(criteria);
    return transactionMapper.toListOfDto(transactions);
  }
}
