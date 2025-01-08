package project.api;

import static project.utility.CommonUtils.ACCOUNTS_ENDPOINT;
import static project.utility.CommonUtils.ACCOUNT_ENDPOINT;
import static project.utility.CommonUtils.ACCOUNT_ID_ENDPOINT;

import project.dto.account.AccountCriteria;
import project.dto.account.AccountResponse;
import project.dto.account.AccountsResponse;
import project.dto.account.CreateAccountRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

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
