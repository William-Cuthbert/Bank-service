package com.project.service.impl;

import com.project.dto.transaction.TransactionCriteria;
import com.project.enums.PaymentResult;
import com.project.enums.PaymentType;
import com.project.repository.entity.Transaction;
import com.project.repository.TransactionRepository;
import com.project.service.paymentLogic.DepositTransactionLogic;
import com.project.service.paymentLogic.RefundTransactionLogic;
import com.project.service.paymentLogic.TransactionLogic;
import com.project.service.TransactionService;

import com.project.service.paymentLogic.TransferTransactionLogic;
import com.project.service.paymentLogic.WithdrawTransactionLogic;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;
  private final TransferTransactionLogic transferTransactionLogic;
  private final RefundTransactionLogic refundTransactionLogic;
  private final DepositTransactionLogic depositTransactionLogic;
  private final WithdrawTransactionLogic withdrawTransactionLogic;
  private final ApplicationContext applicationContext;


  @Autowired
  public TransactionServiceImpl(
      TransactionRepository transactionRepository,
      TransferTransactionLogic transferTransactionLogic,
      RefundTransactionLogic refundTransactionLogic,
      DepositTransactionLogic depositTransactionLogic,
      WithdrawTransactionLogic withdrawTransactionLogic,
      ApplicationContext applicationContext) {
    this.transactionRepository = transactionRepository;
    this.transferTransactionLogic = transferTransactionLogic;
    this.refundTransactionLogic = refundTransactionLogic;
    this.depositTransactionLogic = depositTransactionLogic;
    this.withdrawTransactionLogic = withdrawTransactionLogic;
    this.applicationContext = applicationContext;
  }

  private TransactionLogic getHandler(PaymentType type) {
    switch (type) {
      case TRANSFER:
        return applicationContext.getBean(TransferTransactionLogic.class);
      case DEPOSIT:
        return applicationContext.getBean(DepositTransactionLogic.class);
      case REFUND:
        return applicationContext.getBean(RefundTransactionLogic.class);
      case WITHDRAW:
        return applicationContext.getBean(WithdrawTransactionLogic.class);
      default:
        throw new IllegalArgumentException("No handler found for transaction type: " + type);
    }
  }

  @Override
  public Transaction createTransaction(PaymentType type, String sourceId, String targetId,
                                        double amount, String reference) {
    TransactionLogic handler = getHandler(type);
    if (handler == null) {
      throw new IllegalArgumentException("No handler found for transaction type: " + type);
    }
    if (handler instanceof RefundTransactionLogic) {
      throw new IllegalArgumentException("Refund handler logic is not here: " + type);
    }
    return handler.executeLogic(sourceId, targetId, amount, reference);
  }

  @Override
  public Transaction refundTransaction(String transactionId) {
    TransactionLogic handler = getHandler(PaymentType.REFUND);
    if (handler == null) {
      throw new IllegalArgumentException("Refund handler not found");
    }
    return handler.executeLogic(transactionId, null, 0, null);
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
