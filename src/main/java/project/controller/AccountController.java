package project.controller;

import project.api.AccountApi;

import project.dto.account.AccountCriteria;
import project.dto.account.AccountResponse;
import project.dto.account.AccountsResponse;
import project.dto.account.CreateAccountRequest;
import project.repository.entity.Account;
import project.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@RestController
public class AccountController {

  private final AccountService accountService;

  @Autowired
  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

//  @Override
//  public ResponseEntity<AccountResponse> createAccount(
//      @Valid @RequestBody CreateAccountRequest createAccountRequest) {
//    Account account = accountService.createAccount(createAccountRequest);
//    return new ResponseEntity<>(AccountConverter.convertToAccountResponse(account),
//        HttpStatus.CREATED);
//  }

//  @Override
//  public ResponseEntity<AccountResponse> getAccountBySortCodeAndAccountNumber(
//      @RequestParam @Valid String sortCode, @RequestParam @Valid String accountNumber) {
//    Account account = accountService.getAccountBySortCodeAndAccountNumber(sortCode, accountNumber);
//    return new ResponseEntity<>(AccountConverter.convertToAccountResponse(account), HttpStatus.OK);
//  }

//  @Override
//  public ResponseEntity<AccountsResponse> getAccounts(
//      @Valid final AccountCriteria accountCriteria) {
//    List<Account> accounts = accountService.finalAllAccounts(accountCriteria);
//    return new ResponseEntity<>(AccountConverter.convertAccountsToAccountsResponse(accounts),
//        HttpStatus.OK);
//  }

//  @Override
  public ResponseEntity<AccountResponse> deleteAccount(
      @PathVariable @NotBlank String currentAccountId) {
    Account account = accountService.getAccountIdWithoutStatusCheck(currentAccountId);
    accountService.deleteAccount(account, currentAccountId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
