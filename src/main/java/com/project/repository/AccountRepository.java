package com.project.repository;

import org.springframework.data.repository.CrudRepository;
import com.project.enums.Status;
import com.project.repository.entity.Account;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
    Optional<Account> findBySortCodeAndAccountNumber(String sort, String accountNum);
    Optional<Account> findByEmailAddressOrPhoneNumber(String email, String phone);
    List<Account> findByStatus(Status status);
    Optional<Account> findById(String accountId);
}