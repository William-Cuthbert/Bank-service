package project.service;

import project.repository.entity.Transaction;

public interface TransactionService {

    Transaction Transfer(String currentAccountId, String targetAccountId, double amount, String reference);

    Transaction Refund(String transactionId);

    Transaction Deposit(String accountId, double amount);

    Transaction Withdraw(String accountId, double amount);

    Transaction setDirectDebits(String currentAccountId, String targetAccountId, double amount, String reference);

}
