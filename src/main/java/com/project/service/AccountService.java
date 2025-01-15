package com.project.service;

import java.util.List;
import com.project.dto.account.AccountCriteria;
import com.project.dto.account.CreateAccountRequest;
import com.project.repository.entity.Account;

public interface AccountService {

    Account createAccount(CreateAccountRequest createAccountRequest);

    Account getAccountBySortCodeAndAccountNumber(String sortCode, String accountNumber);

    List<Account> finalAllAccounts(AccountCriteria accountCriteria);

    Account updateAccount(Account account);

    Account deleteAccount(Account account, String id);

    Account getAccountIdWithoutStatusCheck(String accountId);

    Account getAccount(String accountId);

}
