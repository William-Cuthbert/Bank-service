package project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import project.enums.PaymentType;
import project.enums.Status;
import project.repository.entity.Account;
import project.repository.entity.Transaction;
import project.model.DepositRequest;
import project.model.RefundRequest;
import project.model.TransferRequest;
import project.model.WithdrawRequest;
import project.service.impl.AccountServiceImpl;
import project.service.impl.TransactionServiceImpl;
import project.utility.CodeUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static project.utility.CommonUtils.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@WebMvcTest(TransactionRestController.class)
public class TransactionRestControllerTest {

    @MockBean private TransactionServiceImpl transactionService;

    @MockBean private AccountServiceImpl accountService;

    @Autowired private MockMvc mockMvc;

    private String id;
    private String sourceAccountId;
    private String targetAccountId;
    private String name;
    private String bank;
    private String phoneNumber;
    private String emailAddress;
    private double balance;
    private String currency;
    private String accountNumber;
    private String sortCode;
    private String reference;
    private double amount;
    private String startTransaction;
    private String endTransaction;

    @BeforeEach
    void setUpAccountDetails() {
        id = UUID.randomUUID().toString();
        sourceAccountId = UUID.randomUUID().toString();
        targetAccountId = UUID.randomUUID().toString();
        name = "Martin";
        bank = "NatWest";
        phoneNumber = "+447587155942";
        emailAddress = "william.cuthbert@fisglobal.com";
        currency = "GBP";
        balance = 500.00;
        amount = 20.00;
        reference = "unit test";
        accountNumber = CodeUtils.getNewAccountNumber();
        sortCode = CodeUtils.getNewSortCode();
        startTransaction = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        endTransaction = LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }

    @Test
    void postTransfer() throws Exception {
        Account sourceAccount = new Account(sourceAccountId, bank, name, phoneNumber, emailAddress, accountNumber, sortCode, balance, Status.ACTIVATE, new ArrayList<>());
        Account targetAccount = new Account(targetAccountId, bank, name, phoneNumber, emailAddress, accountNumber, sortCode, balance, Status.ACTIVATE, new ArrayList<>());
        Transaction transaction = new Transaction(id, sourceAccountId, targetAccountId, name, amount, currency, startTransaction, endTransaction, reference, PaymentType.TRANSFER);
        TransferRequest transferRequest = new TransferRequest(amount, reference);

        when(transactionService.processTransaction(anyString(), eq(sourceAccountId), eq(targetAccountId), eq(transferRequest.getAmount()), eq(transferRequest.getReference()), eq(
            PaymentType.TRANSFER), eq(null))).thenReturn(transaction);

        mockMvc.perform(MockMvcRequestBuilders.post(TRANSFER_TARGET_ENDPOINT, sourceAccountId, targetAccountId)
                        .content(new ObjectMapper().writeValueAsString(transferRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.sourceAccountId", is(sourceAccountId)))
                .andExpect(jsonPath("$.targetAccountId", is(targetAccountId)))
                .andExpect(jsonPath("$.fullName", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)))
                .andExpect(jsonPath("$.currency", is(currency)))
                .andExpect(jsonPath("$.initiationDate", is(startTransaction)))
                .andExpect(jsonPath("$.completionDate", is(endTransaction)))
                .andExpect(jsonPath("$.reference", is(reference)))
                .andExpect(jsonPath("$.paymentType", is(PaymentType.TRANSFER.getValue())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(transactionService).processTransaction(anyString(), eq(sourceAccountId), eq(targetAccountId), eq(transferRequest.getAmount()), eq(transferRequest.getReference()), eq(
            PaymentType.TRANSFER), eq(null));
    }

    @Test
    void postRefund() throws Exception {
        Account sourceAccount = new Account(sourceAccountId, bank, name, phoneNumber, emailAddress, accountNumber, sortCode, balance, Status.ACTIVATE, new ArrayList<>());
        Account targetAccount = new Account(targetAccountId, bank, name, phoneNumber, emailAddress, accountNumber, sortCode, balance, Status.ACTIVATE, new ArrayList<>());
        Transaction transaction = new Transaction(id, sourceAccountId, targetAccountId, name, amount, currency, startTransaction, endTransaction, reference, PaymentType.REFUND);
        RefundRequest refundRequest = new RefundRequest(id, amount);

        when(transactionService.getTransactionById(id)).thenReturn(transaction);

        mockMvc.perform(MockMvcRequestBuilders.post(REFUND_TARGET_ENDPOINT, sourceAccountId, targetAccountId)
                        .content(new ObjectMapper().writeValueAsString(refundRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.sourceAccountId", is(sourceAccountId)))
                .andExpect(jsonPath("$.targetAccountId", is(targetAccountId)))
                .andExpect(jsonPath("$.fullName", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)))
                .andExpect(jsonPath("$.currency", is(currency)))
                .andExpect(jsonPath("$.initiationDate", is(startTransaction)))
                .andExpect(jsonPath("$.completionDate", is(endTransaction)))
                .andExpect(jsonPath("$.reference", is(reference)))
                .andExpect(jsonPath("$.paymentType", is(PaymentType.REFUND.getValue())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(transactionService).processTransaction(anyString(), eq(sourceAccountId), eq(targetAccountId), eq(refundRequest.getAmount()), eq(reference), eq(
            PaymentType.REFUND), eq(transaction));
    }

    @Test
    void postDeposit() throws Exception {
//        Account targetAccount = new Account(sourceAccountId, bank, name, surname, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNumber, sortCode, balance, currency, new ArrayList<>(), login);
        Transaction transaction = new Transaction(id, sourceAccountId, null, name, amount, currency, startTransaction, endTransaction, DEPOSIT_DEFAULT_REF, PaymentType.DEPOSIT);
        DepositRequest depositRequest = new DepositRequest(accountNumber, amount);

        when(transactionService.processTransaction(anyString(), eq(sourceAccountId), eq(targetAccountId), eq(amount), eq(DEPOSIT_DEFAULT_REF), eq(
            PaymentType.DEPOSIT), eq(null))).thenReturn(transaction);

        mockMvc.perform(MockMvcRequestBuilders.post(DEPOSIT_ENDPOINT)
                        .content(new ObjectMapper().writeValueAsString(depositRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.sourceAccountId", is(sourceAccountId)))
                .andExpect(jsonPath("$.targetAccountId", is(targetAccountId)))
                .andExpect(jsonPath("$.firstName", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)))
                .andExpect(jsonPath("$.currency", is(currency)))
                .andExpect(jsonPath("$.initiationDate", is(startTransaction)))
                .andExpect(jsonPath("$.completionDate", is(endTransaction)))
                .andExpect(jsonPath("$.reference", is(DEPOSIT_DEFAULT_REF)))
                .andExpect(jsonPath("$.paymentType", is(PaymentType.DEPOSIT.getValue())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(transactionService).processTransaction(anyString(), eq(sourceAccountId), eq(null), eq(amount), eq(DEPOSIT_DEFAULT_REF), eq(
            PaymentType.DEPOSIT), eq(null));
    }

    @Test
    void postWithdraw() throws Exception {
        targetAccountId = null;
//        Account sourceAccount = new Account(sourceAccountId, bank, name, surname, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNumber, sortCode, balance, currency, new ArrayList<>(), login);
        Transaction transaction = new Transaction(id, sourceAccountId, null, name, amount, currency, startTransaction, endTransaction, WITHDRAW_DEFAULT_REF, PaymentType.WITHDRAW);
        WithdrawRequest withdrawRequest = new WithdrawRequest(sortCode, accountNumber, amount);

        when(transactionService.processTransaction(anyString(), eq(sourceAccountId), eq(targetAccountId), eq(amount), eq(WITHDRAW_DEFAULT_REF), eq(
            PaymentType.WITHDRAW), eq(null))).thenReturn(transaction);

        mockMvc.perform(MockMvcRequestBuilders.post(WITHDRAWAL_ENDPOINT)
                        .content(new ObjectMapper().writeValueAsString(withdrawRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.sourceAccountId", is(sourceAccountId)))
                .andExpect(jsonPath("$.targetAccountId", is(targetAccountId)))
                .andExpect(jsonPath("$.firstName", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)))
                .andExpect(jsonPath("$.currency", is(currency)))
                .andExpect(jsonPath("$.initiationDate", is(startTransaction)))
                .andExpect(jsonPath("$.completionDate", is(endTransaction)))
                .andExpect(jsonPath("$.reference", is(WITHDRAW_DEFAULT_REF)))
                .andExpect(jsonPath("$.paymentType", is(PaymentType.WITHDRAW.getValue())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(transactionService).processTransaction(anyString(), eq(sourceAccountId), eq(null), eq(amount), eq(WITHDRAW_DEFAULT_REF), eq(
            PaymentType.WITHDRAW), eq(null));
    }
}