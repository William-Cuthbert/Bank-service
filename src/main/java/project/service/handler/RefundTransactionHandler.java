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
public class RefundTransactionHandler implements TransactionHandler {

  private final TransactionRepository transactionRepository;
  private final AccountService accountService;

  @Autowired
  public RefundTransactionHandler(TransactionRepository transactionRepository, AccountService accountService) {
    this.transactionRepository = transactionRepository;
    this.accountService = accountService;
  }

  @Override
  public Transaction handle(String originalTransactionId, String unused, double unusedAmount, String unusedReference) {
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
    refundTransaction.setInitiationDate(LocalDateTime.now().format(DATE_TIME_FORMATTER));
    refundTransaction.setCompletionDate(LocalDateTime.now().format(DATE_TIME_FORMATTER));
    refundTransaction.setType(PaymentType.REFUND);
    refundTransaction.setSourceAccountId(sourceId);
    refundTransaction.setTargetAccountId(targetId);
    refundTransaction.setAmount(amount);
    refundTransaction.setReference("Refund for: " + originalTransaction.getReference());
    refundTransaction.setResult(PaymentResult.AUTHORIZED);

    return transactionRepository.save(refundTransaction);
  }
}

