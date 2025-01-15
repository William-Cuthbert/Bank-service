package com.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.dto.transaction.TransactionCriteria;
import com.project.enums.PaymentType;
import com.project.repository.TransactionRepository;
import com.project.repository.entity.Transaction;
import com.project.service.handler.TransactionHandler;
import com.project.service.impl.TransactionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class TransactionServiceImplTest {

  @Mock
  private TransactionRepository transactionRepository;
  @Mock
  private TransactionHandler transferTransactionHandler;
  @Mock
  private TransactionHandler refundTransactionHandler;
  @Mock
  private TransactionHandler depositTransactionHandler;
  @Mock
  private TransactionHandler withdrawTransactionHandler;

  @InjectMocks
  private TransactionServiceImpl transactionServiceImpl;

  private Transaction transaction;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    transaction = new Transaction("1", "sourceAccountId", "targetAccountId", 100.0, "USD",
        LocalDateTime.now(), LocalDateTime.now(), "ref123", PaymentType.TRANSFER, null);
  }

  @Test
  void testProcessTransaction_withTransfer() {
    PaymentType type = PaymentType.TRANSFER;
    when(transferTransactionHandler.handle(anyString(), anyString(), anyDouble(), anyString()))
        .thenReturn(transaction);
    Transaction result = transactionServiceImpl.processTransaction(type, "sourceId", "targetId", 100.0, "ref123");
    assertNotNull(result);
    assertEquals("sourceId", result.getSourceAccountId());
    assertEquals("targetId", result.getTargetAccountId());
    verify(transferTransactionHandler).handle("sourceId", "targetId", 100.0, "ref123");
  }

  @Test
  void testProcessTransaction_withInvalidType() {
    PaymentType type = PaymentType.REFUND; // no handler mapped for this one
    when(refundTransactionHandler.handle(anyString(), anyString(), anyDouble(), anyString()))
        .thenReturn(transaction);
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      transactionServiceImpl.processTransaction(type, "sourceId", "targetId", 100.0, "ref123");
    });
    assertEquals("No handler found for transaction type: REFUND", exception.getMessage());
  }

  @Test
  void testRefund() {
    when(refundTransactionHandler.handle(anyString(), isNull(), eq(0.0), isNull()))
        .thenReturn(transaction);
    Transaction result = transactionServiceImpl.refund("transactionId");
    assertNotNull(result);
    assertEquals("transactionId", result.getId());
    verify(refundTransactionHandler).handle("transactionId", null, 0, null);
  }

  @Test
  void testFindTransactionsWithFilters() {
    TransactionCriteria criteria = mock(TransactionCriteria.class);
    when(criteria.getAccountId()).thenReturn("sourceAccountId");
    when(transactionRepository.findAll()).thenReturn(Collections.singletonList(transaction));
    List<Transaction> result = transactionServiceImpl.findTransactionsWithFilters(criteria);
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals("sourceAccountId", result.get(0).getSourceAccountId());
  }

  @Test
  void testFindTransactionsWithDateFilter() {
    TransactionCriteria criteria = mock(TransactionCriteria.class);
    when(criteria.getStartDate()).thenReturn("2025-01-01 00:00:00");
    when(criteria.getEndDate()).thenReturn("2025-12-31 23:59:59");
    when(transactionRepository.findAll()).thenReturn(Collections.singletonList(transaction));
    List<Transaction> result = transactionServiceImpl.findTransactionsWithFilters(criteria);
    assertNotNull(result);
    assertFalse(result.isEmpty());
  }
}
