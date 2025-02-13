package com.project.service.paymentLogic;

import com.project.repository.entity.Transaction;

public interface TransactionLogic {
  Transaction executeLogic(String sourceId, String targetId, double amount, String reference);
}
