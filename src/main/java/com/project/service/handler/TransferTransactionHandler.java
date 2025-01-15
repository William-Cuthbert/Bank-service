package com.project.service.handler;

import com.project.enums.PaymentResult;
import com.project.enums.PaymentType;
import com.project.errorhandler.exception.InsufficientBalanceException;
import com.project.repository.TransactionRepository;
import com.project.repository.entity.Account;
import com.project.repository.entity.Transaction;
import com.project.service.AccountService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;

@Slf4j
@Component
public class TransferTransactionHandler implements TransactionHandler {

  private final AccountService accountService;
  private final TransactionRepository transactionRepository;

  @Autowired
  public TransferTransactionHandler(AccountService accountService,
      TransactionRepository transactionRepository) {
    this.accountService = accountService;
    this.transactionRepository = transactionRepository;
  }

  @Transactional
  @Override
  public Transaction handle(String sourceId, String targetId, double amount, String reference) {
    try {
      Account sourceAccount = accountService.getAccount(sourceId);
      Account targetAccount = accountService.getAccount(targetId);
      updateBalancesForTransfer(sourceAccount, targetAccount, amount);
      LocalDateTime now = LocalDateTime.now();
      Transaction transaction = Transaction.builder()
          .id(UUID.randomUUID().toString())
          .sourceAccountId(sourceId)
          .targetAccountId(targetId)
          .amount(amount)
          .currency("USD")
          .initiationDate(now)
          .completionDate(now)
          .reference(reference)
          .type(PaymentType.TRANSFER)
          .result(PaymentResult.AUTHORIZED)
          .build();
      return transactionRepository.save(transaction);
    } catch (OptimisticLockException e) {
      log.error("Concurrency issue detected during transfer: {}", e.getMessage(), e);
      throw new OptimisticLockException("A concurrency issue occurred while processing "
          + "the transaction. Please try again.");
    }
  }

  private void updateBalancesForTransfer(
      Account sourceAccount, Account targetAccount, double amount) {
    if (sourceAccount.getBalance() < amount) {
      throw new InsufficientBalanceException("Insufficient funds in source account.");
    }
    double sourceNewBalance = sourceAccount.getBalance() - amount;
    double targetNewBalance = targetAccount.getBalance() + amount;
    log.info("Initiating transfer: Source Account ID: {}, Amount Deducted: {}, New Balance: {}",
        sourceAccount.getId(), amount, sourceNewBalance);
    log.info("Target Account ID: {}, Amount Added: {}, New Balance: {}",
        targetAccount.getId(), amount, targetNewBalance);
    sourceAccount.setBalance(sourceNewBalance);
    targetAccount.setBalance(targetNewBalance);
    accountService.updateAccount(sourceAccount);
    accountService.updateAccount(targetAccount);
  }
}
