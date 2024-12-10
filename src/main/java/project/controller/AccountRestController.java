package project.controller;

import static project.utility.CommonUtils.ACCOUNTS_ENDPOINT;
import static project.utility.CommonUtils.ACCOUNT_ENDPOINT;
import static project.utility.CommonUtils.ACCOUNT_ID_ENDPOINT;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.api.AccountApi;
import project.converter.AccountConverter;
import project.model.AccountCriteria;
import project.model.AccountResponse;
import project.model.AccountsResponse;
import project.model.CreateAccountRequest;
import project.repository.entity.Account;
import project.service.AccountService;
import project.service.impl.AccountServiceImpl;

@RestController
public class AccountRestController implements AccountApi {

  private final AccountService accountService;

  @Autowired
  public AccountRestController(AccountService accountService) {
    this.accountService = accountService;
  }

  @Override
  public ResponseEntity<AccountResponse> createAccount(
      @Valid @RequestBody CreateAccountRequest createAccountRequest) {
    Account account = accountService.createAccount(createAccountRequest);
    return new ResponseEntity<>(AccountConverter.convertToAccountResponse(account),
        HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<AccountResponse> getAccountBySortCodeAndAccountNumber(
      @RequestParam @Valid String sortCode, @RequestParam @Valid String accountNumber) {
    Account account = accountService.getAccountBySortCodeAndAccountNumber(sortCode, accountNumber);
    return new ResponseEntity<>(AccountConverter.convertToAccountResponse(account), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<AccountsResponse> getAccounts(
      @Valid final AccountCriteria accountCriteria) {
    List<Account> accounts = accountService.finalAllAccounts(accountCriteria);
    return new ResponseEntity<>(AccountConverter.convertAccountsToAccountsResponse(accounts),
        HttpStatus.OK);
  }

  @Override
  public ResponseEntity<AccountResponse> deleteAccount(
      @PathVariable @NotBlank String currentAccountId) {
    Account account = accountService.getAccountIdWithoutStatusCheck(currentAccountId);
    accountService.deleteAccount(account, currentAccountId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
