package project.service.impl;

import project.service.handler.DepositTransactionHandler;
import project.service.handler.TransferTransactionHandler;
import project.service.handler.WithdrawTransactionHandler;
import project.service.TransactionService;
import project.enums.PaymentType;
import project.repository.entity.Transaction;
import project.service.handler.RefundTransactionHandler;
import project.service.TransactionHandler;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

  private final Map<PaymentType, TransactionHandler> handlerMap;

  @Autowired
  public TransactionServiceImpl(List<TransactionHandler> handlers) {
    handlerMap = handlers.stream()
        .collect(Collectors.toMap(this::getPaymentTypeForHandler, Function.identity()));
  }

  private PaymentType getPaymentTypeForHandler(TransactionHandler handler) {
    if (handler instanceof TransferTransactionHandler) return PaymentType.TRANSFER;
    if (handler instanceof RefundTransactionHandler) return PaymentType.REFUND;
    if (handler instanceof DepositTransactionHandler) return PaymentType.DEPOSIT;
    if (handler instanceof WithdrawTransactionHandler) return PaymentType.WITHDRAW;
    throw new IllegalArgumentException("Unsupported handler: " + handler.getClass().getName());
  }

  @Override
  public Transaction processTransaction(PaymentType type, String sourceId, String targetId,
      double amount, String reference) {
    TransactionHandler handler = handlerMap.get(type);
    if (handler == null) {
      throw new IllegalArgumentException("Unsupported transaction type: " + type);
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
}

