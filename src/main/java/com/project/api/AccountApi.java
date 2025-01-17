package com.project.api;

import static com.project.utility.CommonUtils.ACCOUNTS_ENDPOINT;
import static com.project.utility.CommonUtils.ACCOUNT_ENDPOINT;
import static com.project.utility.CommonUtils.ACCOUNT_ID_ENDPOINT;

import com.project.dto.account.AccountCriteria;
import com.project.dto.account.AccountResponse;
import com.project.dto.account.AccountsResponse;
import com.project.dto.account.CreateAccountRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Validated
public interface AccountApi {

  @PostMapping(value = ACCOUNT_ENDPOINT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<AccountResponse> createAccount(
      @Valid @RequestBody CreateAccountRequest createAccountRequest);

  @GetMapping(value = ACCOUNT_ENDPOINT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<AccountResponse> getAccountBySortCodeAndAccountNumber(
      @RequestParam @Valid String sortCode, @RequestParam @Valid String accountNumber);

  @GetMapping(value = ACCOUNTS_ENDPOINT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<AccountsResponse> getAccounts(@Valid final AccountCriteria accountCriteria);

  @DeleteMapping(value = ACCOUNT_ID_ENDPOINT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<AccountResponse> deleteAccount(@PathVariable @NotBlank String currentAccountId);

}
