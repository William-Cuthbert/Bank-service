package com.project.service.paymentLogic;

import com.project.enums.PaymentResult;
import com.project.enums.PaymentType;
import com.project.repository.TransactionRepository;
import com.project.repository.entity.Account;
import com.project.repository.entity.Transaction;

import com.project.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WithdrawTransactionLogic implements TransactionLogic {

  private final AccountService accountService;
  private final TransactionRepository transactionRepository;

  @Autowired
  public WithdrawTransactionLogic(AccountService accountService, TransactionRepository transactionRepository) {
    this.accountService = accountService;
    this.transactionRepository = transactionRepository;
  }

  @Override
  public Transaction executeLogic(String sourceId, String targetId, double amount, String reference) {
    Account sourceAccount = accountService.getAccount(sourceId);
    double newBalance = sourceAccount.getBalance() + amount;
    sourceAccount.setBalance(newBalance);

    accountService.updateAccount(sourceAccount);

    Transaction transaction = new Transaction();
    transaction.setSourceAccountId(sourceId);
    transaction.setTargetAccountId(targetId);
    transaction.setAmount(amount);
    transaction.setReference(reference);
    transaction.setType(PaymentType.WITHDRAW);
    transaction.setResult(PaymentResult.AUTHORIZED);
//    transaction.setInitiationDate(LocalDateTime.now().format(DATE_TIME_FORMATTER));
//    transaction.setCompletionDate(LocalDateTime.now().format(DATE_TIME_FORMATTER));

    transactionRepository.save(transaction);
    return transaction;
  }
}
