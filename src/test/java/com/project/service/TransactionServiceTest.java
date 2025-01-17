package com.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.dto.transaction.TransactionCriteria;
import com.project.enums.PaymentResult;
import com.project.enums.PaymentType;
import com.project.repository.TransactionRepository;
import com.project.repository.entity.Transaction;
import com.project.service.handler.TransactionHandler;
import com.project.service.impl.TransactionServiceImpl;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

  @Mock
  TransactionHandler transferHandler;
  @Mock
  TransactionHandler depositHandler;
  @Mock
  TransactionHandler refundHandler;
  @Mock
  TransactionHandler withdrawHandler;
  @Mock
  TransactionRepository transactionRepository;
  @InjectMocks
  TransactionServiceImpl transactionServiceImpl;

  private Transaction getTransaction() {
    Transaction transaction = new Transaction();
    transaction.setId("transactionId");
    transaction.setSourceAccountId("sourceId");
    transaction.setTargetAccountId("targetId");
    transaction.setType(PaymentType.TRANSFER);
    transaction.setResult(PaymentResult.AUTHORIZED);
    return transaction;
  }

  @Test
  void testInitiate_withTransfer() {
    when(transferHandler.handle(anyString(), anyString(), anyDouble(), anyString()))
        .thenReturn(getTransaction());
    Transaction result = transactionServiceImpl
        .initiate(PaymentType.TRANSFER, "sourceId", "targetId", 100.0, "ref123");
    assertNotNull(result);
    assertEquals("sourceId", result.getSourceAccountId());
    assertEquals("targetId", result.getTargetAccountId());
    verify(transferHandler)
        .handle("sourceId", "targetId", 100.0, "ref123");
  }

  @Test
  void testRefund() {
    getTransaction().setType(PaymentType.REFUND);
    when(refundHandler.handle(anyString(), isNull(), eq(0.0), isNull()))
        .thenReturn(getTransaction());
    Transaction result = transactionServiceImpl.refund("transactionId");
    assertNotNull(result);
    assertEquals("transactionId", result.getId());
    verify(refundHandler).handle("transactionId", null, 0, null);
  }

  @Test
  void testFindTransactionsWithOneFilter() {
    TransactionCriteria criteria = new TransactionCriteria();
    criteria.setAccountId("sourceId");
    when(transactionRepository.findByResult(PaymentResult.AUTHORIZED))
        .thenReturn(Collections.singletonList(getTransaction()));
    List<Transaction> result = transactionServiceImpl.findTransactionsWithFilters(criteria);
    assertFalse(result.isEmpty());
    assertEquals("sourceId", result.get(0).getSourceAccountId());
  }
}
