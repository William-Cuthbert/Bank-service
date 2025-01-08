package project.service;

import project.repository.entity.Transaction;

public interface TransactionHandler {
  Transaction handle(String sourceId, String targetId, double amount, String reference);
}
