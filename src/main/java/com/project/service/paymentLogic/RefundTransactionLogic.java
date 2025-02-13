package com.project.service.paymentLogic;

import com.project.service.AccountService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.project.enums.PaymentResult;
import com.project.enums.PaymentType;
import com.project.repository.TransactionRepository;
import com.project.repository.entity.Account;
import com.project.repository.entity.Transaction;

@Slf4j
@Component
public class RefundTransactionLogic implements TransactionLogic {

  private final TransactionRepository transactionRepository;
  private final AccountService accountService;

  @Autowired
  public RefundTransactionLogic(TransactionRepository transactionRepository, AccountService accountService) {
    this.transactionRepository = transactionRepository;
    this.accountService = accountService;
  }

  @Override
  public Transaction executeLogic(String originalTransactionId, String unused, double unusedAmount, String unusedReference) {
    Transaction originalTransaction = transactionRepository.findById(originalTransactionId)
        .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

    return handleRefund(originalTransaction);
  }

  private Transaction handleRefund(Transaction originalTransaction) {
    String sourceId = originalTransaction.getTargetAccountId();
    String targetId = originalTransaction.getSourceAccountId();
    double amount = originalTransaction.getAmount();

    Account sourceAccount = accountService.getAccount(sourceId);
    Account targetAccount = accountService.getAccount(targetId);

    sourceAccount.setBalance(sourceAccount.getBalance() + amount);
    targetAccount.setBalance(targetAccount.getBalance() - amount);

    accountService.updateAccount(sourceAccount);
    accountService.updateAccount(targetAccount);

    Transaction refundTransaction = new Transaction();
    refundTransaction.setId(UUID.randomUUID().toString());
//    refundTransaction.setInitiationDate(LocalDateTime.now().format(DATE_TIME_FORMATTER));
//    refundTransaction.setCompletionDate(LocalDateTime.now().format(DATE_TIME_FORMATTER));
    refundTransaction.setType(PaymentType.REFUND);
    refundTransaction.setSourceAccountId(sourceId);
    refundTransaction.setTargetAccountId(targetId);
    refundTransaction.setAmount(amount);
    refundTransaction.setReference("Refund for: " + originalTransaction.getReference());
    refundTransaction.setResult(PaymentResult.AUTHORIZED);

    return transactionRepository.save(refundTransaction);
  }
}

