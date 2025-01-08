package project.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import project.enums.PaymentType;
import project.repository.entity.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends CrudRepository<Transaction, String> {

    List<Transaction> findBySourceAccountIdOrderByInitiationDate(String id);

    @Query("SELECT t FROM Transaction t WHERE t.sourceAccountId = :accountId " +
        "AND (:type IS NULL OR t.type = :type) " +
        "AND (:status IS NULL OR t.result = :status) " +
        "AND (:startDate IS NULL OR t.initiationDate >= :startDate) " +
        "AND (:endDate IS NULL OR t.initiationDate <= :endDate)")
    List<Transaction> findByAccountAndFilters(
        @Param("accountId") String accountId,
        @Param("type") PaymentType type,
        @Param("status") String status,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate);
}
