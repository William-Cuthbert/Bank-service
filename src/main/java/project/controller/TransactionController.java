package project.controller;

import project.enums.PaymentType;
import project.repository.entity.Transaction;
import project.service.TransactionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

  private final TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  /**
   * Process a transaction of any type: DEPOSIT, WITHDRAW, TRANSFER, or REFUND.
   *
   * @param type      the type of transaction (required)
   * @param sourceId  the source account ID (required)
   * @param targetId  the target account ID (optional, only for TRANSFER or REFUND)
   * @param amount    the transaction amount (must be positive)
   * @param reference a reference or description for the transaction (required)
   * @return ResponseEntity containing the processed transaction.
   */
  @PostMapping("/process")
  public ResponseEntity<Transaction> processTransaction(
      @RequestParam @NotNull PaymentType type,
      @RequestParam @NotNull String sourceId,
      @RequestParam(required = false) String targetId,
      @RequestParam @Min(value = 0, message = "Amount must be positive") double amount,
      @RequestParam @NotNull String reference) {
    Transaction transaction = transactionService.processTransaction(type, sourceId, targetId, amount, reference);
    return ResponseEntity.ok(transaction);
  }

  /**
   * Refund an existing transaction by its ID.
   *
   * @param transactionId the ID of the transaction to refund (required)
   * @return ResponseEntity containing the refund transaction.
   */
  @PostMapping("/refund/{transactionId}")
  public ResponseEntity<Transaction> refundTransaction(@PathVariable @NotNull String transactionId) {
    Transaction refundTransaction = transactionService.refund(transactionId);
    return ResponseEntity.ok(refundTransaction);
  }
}
