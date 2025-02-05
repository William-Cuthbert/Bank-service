package com.project.controller;

import com.project.api.AccountApi;
import com.project.dto.account.AccountResponse;
import com.project.dto.account.CreateAccountRequest;
import com.project.repository.entity.Account;
import com.project.service.AccountService;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController
public class AccountController implements AccountApi {

  private final AccountService accountService;

  @Autowired
  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @Override
  public ResponseEntity<Account> createAccount(
      @Valid @RequestBody CreateAccountRequest createAccountRequest) {
    Account account = accountService.createAccount(createAccountRequest);
    return new ResponseEntity<>(account, HttpStatus.CREATED);
  }

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
