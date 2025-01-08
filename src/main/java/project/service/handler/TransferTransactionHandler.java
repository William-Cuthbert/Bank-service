package project.service.handler;

import static project.utility.CommonUtils.DATE_TIME_FORMATTER;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.enums.PaymentResult;
import project.enums.PaymentType;
import project.repository.TransactionRepository;
import project.repository.entity.Account;
import project.repository.entity.Transaction;
import project.service.AccountService;
import project.service.TransactionHandler;

@Service
public class TransferTransactionHandler implements TransactionHandler {

  private final AccountService accountService;
  private final TransactionRepository transactionRepository;

  @Autowired
  public TransferTransactionHandler(AccountService accountService, TransactionRepository transactionRepository) {
    this.accountService = accountService;
    this.transactionRepository = transactionRepository;
  }

  @Override
  public Transaction handle(String sourceId, String targetId, double amount, String reference) {
    Account sourceAccount = accountService.getAccount(sourceId);
    Account targetAccount = accountService.getAccount(targetId);

    if (sourceAccount.getBalance() < amount) {
      throw new IllegalArgumentException("Insufficient funds for transfer");
    }

    sourceAccount.setBalance(sourceAccount.getBalance() - amount);
    targetAccount.setBalance(targetAccount.getBalance() + amount);

    accountService.updateAccount(sourceAccount);
    accountService.updateAccount(targetAccount);

    Transaction transaction = new Transaction();
    transaction.setId(UUID.randomUUID().toString());
    transaction.setInitiationDate(LocalDateTime.now().format(DATE_TIME_FORMATTER));
    transaction.setCompletionDate(LocalDateTime.now().format(DATE_TIME_FORMATTER));
    transaction.setType(PaymentType.TRANSFER);
    transaction.setSourceAccountId(sourceId);
    transaction.setTargetAccountId(targetId);
    transaction.setAmount(amount);
    transaction.setReference(reference);
    transaction.setResult(PaymentResult.AUTHORIZED);

    return transactionRepository.save(transaction);
  }
}

