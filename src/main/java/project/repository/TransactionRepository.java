package project.repository;

import project.repository.entity.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String>,
        JpaSpecificationExecutor<Transaction> {
    List<Transaction> findBySourceAccountIdOrderByInitiationDate(String sourceAccountId);
}
