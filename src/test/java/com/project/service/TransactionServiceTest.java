package com.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.dto.transaction.TransactionCriteria;
import com.project.enums.PaymentResult;
import com.project.enums.PaymentType;
import com.project.repository.TransactionRepository;
import com.project.repository.entity.Transaction;
import com.project.service.impl.TransactionServiceImpl;

import com.project.service.paymentLogic.DepositTransactionLogic;
import com.project.service.paymentLogic.RefundTransactionLogic;
import com.project.service.paymentLogic.TransferTransactionLogic;
import com.project.service.paymentLogic.WithdrawTransactionLogic;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

  @Mock
  TransferTransactionLogic transferTransactionLogic;
  @Mock
  DepositTransactionLogic depositTransactionLogic;
  @Mock
  RefundTransactionLogic refundTransactionLogic;
  @Mock
  WithdrawTransactionLogic withdrawTransactionLogic;
  @Mock
  ApplicationContext applicationContext;
  @Mock
  TransactionRepository transactionRepository;
  @InjectMocks
  TransactionServiceImpl transactionServiceImpl;

  private Transaction getValidTransaction(PaymentType type) {
    Transaction transaction = new Transaction();
    transaction.setId("transactionId");
    transaction.setResult(PaymentResult.AUTHORIZED);
    transaction.setSourceAccountId("sourceId");
    switch (type) {
      case TRANSFER:
      case REFUND:
        transaction.setTargetAccountId("targetId");
        transaction.setType(type);
        break;
      case DEPOSIT:
      case WITHDRAW:
        transaction.setTargetAccountId(null);
        transaction.setType(type);
        break;
      default:
        break;
    }
    return transaction;
  }

  @Test
  void testMakePayment_withTransfer() {
    when(applicationContext.getBean(TransferTransactionLogic.class)).thenReturn(transferTransactionLogic);
    when(transferTransactionLogic.executeLogic(anyString(), anyString(), anyDouble(), anyString())).thenReturn(
        getValidTransaction(PaymentType.TRANSFER));
    Transaction result = transactionServiceImpl.createTransaction(PaymentType.TRANSFER, "sourceId",
        "targetId", 100.0, "ref123");
    assertNotNull(result);
    assertEquals("sourceId", result.getSourceAccountId());
    assertEquals("targetId", result.getTargetAccountId());
    verify(transferTransactionLogic).executeLogic("sourceId", "targetId", 100.0, "ref123");
  }

  @Test
  void testMakePayment_withDeposit() {
    when(applicationContext.getBean(DepositTransactionLogic.class)).thenReturn(depositTransactionLogic);
    when(depositTransactionLogic.executeLogic(anyString(), isNull(), anyDouble(), anyString())).thenReturn(
        getValidTransaction(PaymentType.DEPOSIT));
    Transaction result = transactionServiceImpl.createTransaction(PaymentType.DEPOSIT, "sourceId",
        null, 100.0, "ref123");
    assertNotNull(result);
    assertEquals("sourceId", result.getSourceAccountId());
    assertNull(result.getTargetAccountId());
    verify(depositTransactionLogic).executeLogic("sourceId", null, 100.0, "ref123");
  }

  @Test
  void testMakePayment_withWithdraw() {
    when(applicationContext.getBean(WithdrawTransactionLogic.class)).thenReturn(withdrawTransactionLogic);
    when(withdrawTransactionLogic.executeLogic(anyString(), isNull(), anyDouble(), anyString())).thenReturn(
        getValidTransaction(PaymentType.WITHDRAW));
    Transaction result = transactionServiceImpl.createTransaction(PaymentType.WITHDRAW, "sourceId",
        null, 100.0, "ref123");
    assertNotNull(result);
    assertEquals("sourceId", result.getSourceAccountId());
    assertNull(result.getTargetAccountId());
    verify(withdrawTransactionLogic).executeLogic("sourceId", null, 100.0, "ref123");
  }

  @Test
  void testRefundTransaction() {
    when(applicationContext.getBean(RefundTransactionLogic.class)).thenReturn(refundTransactionLogic);
    when(refundTransactionLogic.executeLogic(anyString(), isNull(), eq(0.0), isNull())).thenReturn(
        getValidTransaction(PaymentType.REFUND));
    Transaction result = transactionServiceImpl.refundTransaction("transactionId");
    assertNotNull(result);
    assertEquals("transactionId", result.getId());
    verify(refundTransactionLogic).executeLogic("transactionId", null, 0, null);
  }

  @Test
  void testFindTransactionsWithOneFilter() {
    TransactionCriteria criteria = new TransactionCriteria();
    criteria.setAccountId("sourceId");
    when(transactionRepository.findByResult(PaymentResult.AUTHORIZED)).thenReturn(
        Collections.singletonList(getValidTransaction(PaymentType.TRANSFER)));
    List<Transaction> result = transactionServiceImpl.findTransactionsWithFilters(criteria);
    assertFalse(result.isEmpty());
    assertEquals("sourceId", result.get(0).getSourceAccountId());
  }
}
