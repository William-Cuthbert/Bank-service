package project.controller;

import static project.utility.CommonUtils.DEPOSIT_DEFAULT_REF;
import static project.utility.CommonUtils.DEPOSIT_ENDPOINT;
import static project.utility.CommonUtils.REFUND_TARGET_ENDPOINT;
import static project.utility.CommonUtils.TRANSFER_TARGET_ENDPOINT;
import static project.utility.CommonUtils.WITHDRAWAL_ENDPOINT;
import static project.utility.CommonUtils.WITHDRAW_DEFAULT_REF;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.converter.TransactionConverter;
import project.enums.PaymentType;
import project.model.DepositRequest;
import project.model.RefundRequest;
import project.model.TransactionResponse;
import project.model.TransferRequest;
import project.model.WithdrawRequest;
import project.repository.entity.Account;
import project.repository.entity.Transaction;
import project.service.impl.AccountServiceImpl;
import project.service.impl.TransactionServiceImpl;

@RestController
public class TransactionRestController {

  private final AccountServiceImpl accountService;
  private final TransactionServiceImpl transactionService;

  @Autowired
  public TransactionRestController(AccountServiceImpl accountService,
      TransactionServiceImpl transactionService) {
    this.accountService = accountService;
    this.transactionService = transactionService;
  }

  @PostMapping(value = TRANSFER_TARGET_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionResponse> postTransfer(
      @PathVariable @NotBlank String currentAccountId,
      @PathVariable @NotBlank String targetAccountId,
      @Valid @RequestBody TransferRequest transferRequest) {
    Transaction transaction = transactionService.processTransaction(
        currentAccountId, targetAccountId, null, transferRequest.getAmount(),
        transferRequest.getReference(), PaymentType.TRANSFER);
    return new ResponseEntity<>(TransactionConverter.convertToTransactionResponse(transaction),
        HttpStatus.OK);
  }

  @PostMapping(value = DEPOSIT_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionResponse> postDeposit(
      @Valid @RequestBody DepositRequest depositRequest) {
    Account account = accountService.getAccountBySortCodeAndAccountNumber(
        depositRequest.getSortCode(),
        depositRequest.getAccountNumber());
    Transaction transaction = transactionService.processTransaction(account.getId(), null, null,
        depositRequest.getAmount(), DEPOSIT_DEFAULT_REF,
        PaymentType.DEPOSIT);
    return new ResponseEntity<>(TransactionConverter.convertToTransactionResponse(transaction),
        HttpStatus.OK);
  }

  @PostMapping(value = WITHDRAWAL_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionResponse> postWithdraw(
      @Valid @RequestBody WithdrawRequest withdrawRequest) {
    Account account = accountService.getAccountBySortCodeAndAccountNumber(
        withdrawRequest.getSortCode(), withdrawRequest.getAccountNumber());
    Transaction transaction = transactionService.processTransaction(
        account.getId(), null, null, withdrawRequest.getAmount(), WITHDRAW_DEFAULT_REF,
        PaymentType.WITHDRAW);
    return new ResponseEntity<>(TransactionConverter.convertToTransactionResponse(transaction),
        HttpStatus.OK);
  }

  @PostMapping(value = REFUND_TARGET_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionResponse> postRefund(
      @PathVariable @NotBlank String currentAccountId,
      @PathVariable @NotBlank String targetAccountId,
      @Valid @RequestBody RefundRequest refundRequest) {
    Transaction transaction = transactionService.processTransaction(
        currentAccountId,
        targetAccountId, refundRequest.getTransactionId(), refundRequest.getAmount(), "",
        PaymentType.REFUND);
    return new ResponseEntity<>(TransactionConverter.convertToTransactionResponse(transaction),
        HttpStatus.OK);
  }
}
