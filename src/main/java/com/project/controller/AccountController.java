package com.project.controller;

import static com.project.utility.ApiUtils.ACCOUNTS_ENDPOINT;
import static com.project.utility.ApiUtils.ACCOUNT_ENDPOINT;
import static com.project.utility.ApiUtils.ACCOUNT_ID_ENDPOINT;

import com.project.dto.account.AccountCriteria;
import com.project.dto.account.AccountDtoResponse;
import com.project.dto.account.AccountDtoRequest;
import com.project.repository.entity.Account;
import com.project.service.AccountService;

import com.project.service.mapper.AccountMapper;
import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RequestMapping("/v1/api")
@RestController
public class AccountController {

  private final AccountService accountService;
  private final AccountMapper accountMapper;

  @Autowired
  public AccountController(AccountService accountService, AccountMapper accountMapper) {
    this.accountService = accountService;
    this.accountMapper = accountMapper;
  }

  @PostMapping(value = ACCOUNT_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public AccountDtoResponse createAccount(@Valid @RequestBody AccountDtoRequest createAccountRequest) {
    Account account = accountService.createAccount(createAccountRequest);
    return accountMapper.toDto(account);
  }

  @GetMapping(value = ACCOUNTS_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public List<AccountDtoResponse> getAccounts(@Valid @RequestBody AccountCriteria accountCriteria) {
    List<Account> accounts = accountService.findAllAccounts(accountCriteria);
    return accountMapper.toListOfDto(accounts);
  }

  @DeleteMapping(value = ACCOUNT_ID_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deleteAccount(@PathVariable @NotBlank String currentAccountId) {
    Account account = accountService.getAccountIdWithoutStatusCheck(currentAccountId);
    accountService.deleteAccount(account, currentAccountId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
