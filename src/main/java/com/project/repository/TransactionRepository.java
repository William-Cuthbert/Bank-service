package com.project.repository;

import com.project.enums.PaymentResult;
import com.project.repository.entity.Transaction;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findBySourceAccountIdOrderByInitiationDate(String sourceAccountId);
    List<Transaction> findByResult(PaymentResult result);
}
