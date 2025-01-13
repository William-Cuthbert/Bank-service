package project.service.handler;

import static project.utility.CommonUtils.DATE_TIME_FORMATTER;

import project.enums.PaymentResult;
import project.enums.PaymentType;
import project.repository.TransactionRepository;
import project.repository.entity.Account;
import project.repository.entity.Transaction;
import project.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DepositTransactionHandler implements TransactionHandler {

  private final AccountService accountService;
  private final TransactionRepository transactionRepository;

  @Autowired
  public DepositTransactionHandler(AccountService accountService, TransactionRepository transactionRepository) {
    this.accountService = accountService;
    this.transactionRepository = transactionRepository;
  }

  @Override
  public Transaction handle(String sourceId, String targetId, double amount, String reference) {
    Account sourceAccount = accountService.getAccount(sourceId);
    double newBalance = sourceAccount.getBalance() - amount;
    sourceAccount.setBalance(newBalance);

    accountService.updateAccount(sourceAccount);

    Transaction transaction = new Transaction();
    transaction.setSourceAccountId(sourceId);
    transaction.setTargetAccountId(targetId);
    transaction.setAmount(amount);
    transaction.setReference(reference);
    transaction.setType(PaymentType.DEPOSIT);
    transaction.setResult(PaymentResult.AUTHORIZED);
    transaction.setInitiationDate(LocalDateTime.now().format(DATE_TIME_FORMATTER));
    transaction.setCompletionDate(LocalDateTime.now().format(DATE_TIME_FORMATTER));

    transactionRepository.save(transaction);
    return transaction;
  }
}

