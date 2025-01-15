package com.project.service.impl;

import static com.project.utility.CommonUtils.DATE_TIME_FORMATTER;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeParseException;
import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
  public List<Transaction> findTransactionsWithFilters(TransactionCriteria criteria) {
    return transactionRepository.findAll()
            .stream()
            .filter(isAccountIdCriteriaMet(criteria))
            .filter(isPaymentTypeCriteriaMet(criteria))
            .filter(isPaymentResultCriteriaMet(criteria))
            .filter(isDateCriteriaMet(criteria))
            .collect(Collectors.toList());
  }

  private Predicate<Transaction> isAccountIdCriteriaMet(TransactionCriteria criteria) {
    return transaction -> {
      String accountId = criteria.getAccountId();
      return accountId == null || accountId.equals(transaction.getSourceAccountId())
          || accountId.equals(transaction.getTargetAccountId());
    };
  }

  private Predicate<Transaction> isPaymentTypeCriteriaMet(TransactionCriteria criteria) {
    final boolean isTypeEmpty = criteria.getType() == null;
    return transaction -> !isTypeEmpty ? criteria.getType().equals(
        transaction.getType()) : Boolean.TRUE;
  }

  private Predicate<Transaction> isPaymentResultCriteriaMet(TransactionCriteria criteria) {
    final boolean isResultEmpty = criteria.getResult() == null;
    return transaction -> !isResultEmpty ? criteria.getResult().equals(
        transaction.getResult()) : Boolean.TRUE;
  }

  private Predicate<Transaction> isDateCriteriaMet(TransactionCriteria criteria) {
    String startDateStr = criteria.getStartDate();
    String endDateStr = criteria.getEndDate();

    LocalDateTime startDate = parseDate(startDateStr);
    LocalDateTime endDate = parseDate(endDateStr);

    return transaction -> (startDate == null || transaction.getInitiationDate()
        .isAfter(startDate) || transaction.getInitiationDate().isEqual(startDate))
        && (endDate == null || transaction.getInitiationDate().isBefore(endDate) ||
        transaction.getInitiationDate().isEqual(endDate));
  }

  private LocalDateTime parseDate(String dateTimeStr) {
    if (dateTimeStr == null || dateTimeStr.isEmpty()) {
      return null;
    }
    try {
      return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid date format: " + dateTimeStr);
    }
  }
}
