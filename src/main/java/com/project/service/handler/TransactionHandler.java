package com.project.service.handler;

import com.project.repository.entity.Transaction;

public interface TransactionHandler {
  Transaction handle(String sourceId, String targetId, double amount, String reference);
}
