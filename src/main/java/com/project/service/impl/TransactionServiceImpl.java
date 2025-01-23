package com.project.service.impl;

import com.project.dto.transaction.TransactionCriteria;
import com.project.enums.PaymentResult;
import com.project.enums.PaymentType;
import com.project.repository.entity.Transaction;
import com.project.repository.TransactionRepository;
import com.project.service.handler.RefundTransactionHandler;
import com.project.service.handler.TransactionHandler;
import com.project.service.TransactionService;

import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

  @Qualifier("transaction")
  private TransactionRepository transactionRepository;
  @Qualifier("transferHandler")
  private TransactionHandler transferHandler;
  @Qualifier("depositHandler")
  private TransactionHandler depositHandler;
  @Qualifier("refundHandler")
  private TransactionHandler refundHandler;
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
    return transactionRepository.findByResult(PaymentResult.AUTHORIZED)
            .stream()
            .filter(isSourceIdValid(criteria))
            .filter(isReferenceValid(criteria))
            .filter(isTypeValid(criteria))
            .collect(Collectors.toList());
  }

  private Predicate<Transaction> isSourceIdValid(TransactionCriteria criteria) {
    boolean isSourceIdEmpty = criteria.getAccountId() == null;
    return transaction -> !isSourceIdEmpty ? criteria.getAccountId()
        .equals(transaction.getSourceAccountId()) : Boolean.TRUE;
  }

  private Predicate<Transaction> isReferenceValid(TransactionCriteria criteria) {
    boolean isRefEmpty = criteria.getReference() == null;
    return transaction -> !isRefEmpty ? criteria.getReference()
        .equals(transaction.getReference()) : Boolean.TRUE;
  }

  private Predicate<Transaction> isTypeValid(TransactionCriteria criteria) {
    boolean isTypeEmpty = criteria.getType() == null;
    return transaction -> !isTypeEmpty ? criteria.getType()
        .equals(transaction.getType()) : Boolean.TRUE;
  }
}
