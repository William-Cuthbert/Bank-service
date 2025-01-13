package project.service;

import project.dto.transaction.TransactionCriteria;
import project.enums.PaymentType;
import project.repository.entity.Transaction;

import java.util.List;

public interface TransactionService {

  /**
   * Process a transaction based on its type, source, and target accounts, amount, and reference.
   *
   * @param type      the type of transaction (DEPOSIT, WITHDRAW, TRANSFER, REFUND)
   * @param sourceId  the source account ID
   * @param targetId  the target account ID (maybe null for DEPOSIT and WITHDRAW)
   * @param amount    the transaction amount
   * @param reference a reference note for the transaction
   * @return the completed Transaction object
   */
  Transaction processTransaction(PaymentType type, String sourceId, String targetId, double amount,
      String reference);

  /**
   * Process a refund for a specific transaction ID.
   *
   * @param transactionId the ID of the transaction to refund
   * @return the refund transaction
   */
  Transaction refund(String transactionId);

  /**
   * finds transaction details with filters.
   *
   * @param transactionCriteria filter parameters to search
   * @return the list of transactions
   */
  List<Transaction> findTransactionsWithFilters(TransactionCriteria transactionCriteria);
}
