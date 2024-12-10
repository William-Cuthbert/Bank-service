package project.service;

import javax.validation.constraints.NotBlank;
import project.model.AccountCriteria;
import project.model.CreateAccountRequest;
import project.repository.entity.Account;

import java.util.List;

public interface AccountService {

    Account createAccount(CreateAccountRequest createAccountRequest);

    Account getAccountBySortCodeAndAccountNumber(String sortCode, String accountNumber);

    List<Account> finalAllAccounts(AccountCriteria accountCriteria);

    Account updateAccount(Account account);

    Account deleteAccount(Account account, String id);

    Account getAccountIdWithoutStatusCheck(String accountId);

    Account getAccount(String accountId);

}
