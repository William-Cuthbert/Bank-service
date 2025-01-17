package com.project.service.impl;

import static com.project.utility.CommonUtils.DATE_TIME_FORMATTER;
import com.project.dto.transaction.TransactionCriteria;
import com.project.enums.PaymentResult;
import com.project.enums.PaymentType;
import com.project.repository.entity.Transaction;
import com.project.repository.TransactionRepository;
import com.project.service.handler.RefundTransactionHandler;
import com.project.service.handler.TransactionHandler;
import com.project.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeParseException;
import java.time.LocalDateTime;
import java.util.function.Predicate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

  @Autowired
  @Qualifier("transaction")
  private TransactionRepository transactionRepository;

  @Autowired
  @Qualifier("transferHandler")
  private TransactionHandler transferHandler;

  @Autowired
  @Qualifier("depositHandler")
  private TransactionHandler depositHandler;

  @Autowired
  @Qualifier("refundHandler")
  private TransactionHandler refundHandler;

  @Autowired
  @Qualifier("withdrawHandler")
  private TransactionHandler withdrawHandler;

  private TransactionHandler getHandler(PaymentType type) {
    switch (type) {
      case TRANSFER:
        return transferHandler;
      case DEPOSIT:
        return depositHandler;
      case REFUND:
        return refundHandler;
      case WITHDRAW:
        return withdrawHandler;
      default:
        return null;
    }
  }

  @Override
  public Transaction initiate(PaymentType type, String sourceId, String targetId,
                                        double amount, String reference) {
    TransactionHandler handler = getHandler(type);
    if (handler == null) {
      throw new IllegalArgumentException("No handler found for transaction type: " + type);
    }
    if (handler instanceof RefundTransactionHandler) {
      throw new IllegalArgumentException("Refund handler logic is not here: " + type);
    }
    return handler.handle(sourceId, targetId, amount, reference);
  }

  @Override
  public Transaction refund(String transactionId) {
    TransactionHandler handler = getHandler(PaymentType.REFUND);
    if (handler == null) {
      throw new IllegalArgumentException("Refund handler not found");
    }
    return handler.handle(transactionId, null, 0, null);
  }

  @Override
  public List<Transaction> findTransactionsWithFilters(TransactionCriteria criteria) {
    List<Transaction> transactions = transactionRepository.findByResult(PaymentResult.AUTHORIZED);
    if (criteria.getAccountId() != null) {
      transactions.stream().filter(transaction ->
          transaction.getSourceAccountId().equals(criteria.getAccountId()));
    }
    if (criteria.getReference() != null) {
      transactions.stream().filter(transaction ->
          transaction.getReference().equals(criteria.getReference()));
    }
    if (criteria.getType() != null) {
      transactions.stream().filter(transaction ->
          transaction.getType() == criteria.getType());
    }
    return transactions;
  }
}
