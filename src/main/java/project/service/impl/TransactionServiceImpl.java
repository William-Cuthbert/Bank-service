package project.service.impl;

import static project.utility.CommonUtils.DATE_TIME_FORMATTER;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.enums.PaymentResult;
import project.enums.PaymentType;
import project.exception.AccountNotFoundException;
import project.repository.TransactionRepository;
import project.repository.entity.Account;
import project.repository.entity.Transaction;
import project.service.AccountService;
import project.service.TransactionService;

import java.time.LocalDateTime;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    @Autowired
    public TransactionServiceImpl(AccountService accountService, TransactionRepository transactionRepository) {
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
    }

    private boolean isBalanceVerified(double amount, double currentAccountAmount) {
        return (currentAccountAmount - amount) >= 0;
    }

    private void updateBalance(Account currentAccount, Account targetAccount, double amount,
        PaymentType payment) {
        double newBalance;
        switch (payment) {
            case DEPOSIT:
                newBalance = currentAccount.getBalance() + amount;
                currentAccount.setBalance(newBalance);
                break;
            case WITHDRAW:
                newBalance = currentAccount.getBalance() - amount;
                currentAccount.setBalance(newBalance);
                break;
            case TRANSFER:
                newBalance = currentAccount.getBalance() - amount;
                currentAccount.setBalance(newBalance);

                newBalance = targetAccount.getBalance() + amount;
                targetAccount.setBalance(newBalance);
                break;
            case REFUND:
                newBalance = currentAccount.getBalance() + amount;
                currentAccount.setBalance(newBalance);

                newBalance = targetAccount.getBalance() - amount;
                targetAccount.setBalance(newBalance);
                break;
        }
        // todo - maybe add updateAccount for fresh balances in account service?
//        accountRepository.save(currentAccount);
    }

    @Override
    public Transaction Transfer(String currentAccountId, String targetAccountId, double amount,
        String reference) {
        log.info("Entering Transfer of TransactionServiceImpl");
        Transaction transactionToSave = new Transaction();
        try {
            Account currentAccount = accountService.getAccount(currentAccountId);
            String fullName = currentAccount.getFullName();
            String initiatedDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);
            transactionToSave.setId(UUID.randomUUID().toString());
            transactionToSave.setInitiationDate(initiatedDate);
            transactionToSave.setType(PaymentType.TRANSFER);
            transactionToSave.setCurrency("currency");
            transactionToSave.setFullName(fullName);
            transactionToSave.setSourceAccountId(currentAccountId);
            transactionToSave.setTargetAccountId(targetAccountId);
            transactionToSave.setReference(reference);
            transactionToSave.setAmount(amount);
            log.info("Setting up a transfer from={} for amount={} reference={}", fullName, amount,
                reference);
            if (isBalanceVerified(amount, currentAccount.getBalance())) {
                Account targetAccount = accountService.getAccount(targetAccountId);
                updateBalance(currentAccount, targetAccount, amount, PaymentType.TRANSFER);
                Thread.sleep(2000);
                transactionToSave.setCompletionDate(
                    LocalDateTime.now().format(DATE_TIME_FORMATTER));
                transactionToSave.setResult(PaymentResult.AUTHORIZED);
            } else {
                transactionToSave.setCompletionDate("Not Completed");
                transactionToSave.setResult(PaymentResult.DECLINED);
            }
            transactionToSave = transactionRepository.save(transactionToSave);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return transactionToSave;
    }

    @Override
    public Transaction Refund(String transactionId) {
        log.info("Entering Refund of TransactionServiceImpl");
        Transaction existingTransaction;
        Transaction transactionToSave = new Transaction();
        try {
            Account currentAccount = accountService.getAccount(currentAccountId);
            String fullName = currentAccount.getFullName();
            String initiatedDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);
            transactionToSave.setId(UUID.randomUUID().toString());
            transactionToSave.setInitiationDate(initiatedDate);
            transactionToSave.setType(PaymentType.TRANSFER);
            transactionToSave.setCurrency("currency");
            transactionToSave.setFullName(fullName);
            transactionToSave.setSourceAccountId(currentAccountId);
            transactionToSave.setTargetAccountId(targetAccountId);
            transactionToSave.setReference(reference);
            transactionToSave.setAmount(amount);
            log.info("Setting up a transfer from={} for amount={} reference={}", fullName, amount,
                reference);
            if (isBalanceVerified(amount, currentAccount.getBalance())) {
                Account targetAccount = accountService.getAccount(targetAccountId);
                updateBalance(currentAccount, targetAccount, amount, PaymentType.TRANSFER);
                Thread.sleep(2000);
                transactionToSave.setCompletionDate(
                    LocalDateTime.now().format(DATE_TIME_FORMATTER));
                transactionToSave.setResult(PaymentResult.AUTHORIZED);
            } else {
                transactionToSave.setCompletionDate("Not Completed");
                transactionToSave.setResult(PaymentResult.DECLINED);
            }
            transactionToSave = transactionRepository.save(transactionToSave);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return transactionToSave;
    }

    @Override
    public Transaction Deposit(String accountId, double amount) {
        return null;
    }

    @Override
    public Transaction Withdraw(String accountId, double amount) {
        return null;
    }

    @Override
    public Transaction setDirectDebits(String currentAccountId, String targetAccountId, double amount, String reference) {
        return null;
    }
}
