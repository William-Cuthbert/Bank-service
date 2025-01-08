package project.service.handler;

import project.repository.entity.Transaction;
import project.service.TransactionHandler;

public class WithdrawTransactionHandler implements TransactionHandler {

  @Override
  public Transaction handle(String sourceId, String targetId, double amount, String reference) {
    return null;
  }
}
