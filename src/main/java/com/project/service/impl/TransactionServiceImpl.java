package com.project.service.impl;

import com.project.dto.transaction.TransactionCriteria;
import com.project.enums.PaymentType;
import com.project.repository.entity.Transaction;
import com.project.repository.TransactionRepository;
import com.project.service.handler.DepositTransactionHandler;
import com.project.service.handler.TransferTransactionHandler;
import com.project.service.handler.WithdrawTransactionHandler;
import com.project.service.handler.RefundTransactionHandler;
import com.project.service.handler.TransactionHandler;
import com.project.service.TransactionService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

  private final Map<PaymentType, TransactionHandler> handlerMap;
  private final TransactionRepository transactionRepository;

  @Autowired
  public TransactionServiceImpl(List<TransactionHandler> handlers, TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
    handlerMap = handlers.stream()
            .collect(Collectors.toMap(this::getPaymentTypeForHandler, Function.identity()));
  }

  private PaymentType getPaymentTypeForHandler(TransactionHandler handler) {
    if (handler instanceof TransferTransactionHandler) return PaymentType.TRANSFER;
    if (handler instanceof RefundTransactionHandler) return PaymentType.REFUND;
    if (handler instanceof DepositTransactionHandler) return PaymentType.DEPOSIT;
    if (handler instanceof WithdrawTransactionHandler) return PaymentType.WITHDRAW;
    throw new IllegalStateException("Unsupported handler type for: " + handler.getClass().getName());
  }

  @Override
  public Transaction processTransaction(PaymentType type, String sourceId, String targetId,
                                        double amount, String reference) {
    TransactionHandler handler = handlerMap.get(type);
    if (handler == null) {
      throw new IllegalArgumentException("No handler found for transaction type: " + type);
    }
    return handler.handle(sourceId, targetId, amount, reference);
  }

  @Override
  public Transaction refund(String transactionId) {
    TransactionHandler handler = handlerMap.get(PaymentType.REFUND);
    if (handler == null) {
      throw new IllegalArgumentException("Refund handler not found");
    }
    return handler.handle(transactionId, null, 0, null);
  }

  @Override
  public List<Transaction> findTransactionsWithFilters(TransactionCriteria transactionCriteria) {
//    List<Transaction> transactions = transactionRepository.findAll().stream().
//    return transactionRepository.findAll(spec);
    return null;
  }
}


