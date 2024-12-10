package project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import project.enums.PaymentType;
import project.enums.Status;
import project.exception.AccountNotFoundException;
import project.exception.TransactionNotFoundException;
import project.repository.AccountRepository;
import project.repository.TransactionRepository;
import project.repository.entity.Account;
import project.repository.entity.Transaction;
import project.service.impl.TransactionServiceImpl;
import project.utility.CodeUtils;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static project.utility.CommonUtils.DATE_TIME_FORMATTER;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock Account currentAccount;
    @Mock Account targetAccount;
    String id;
    String sourceAccountId;
    String targetAccountId;
    String name;
    String targetAccountFirstName;
    String surname;
    String targetAccountSurname;
    String bankName;
    String phoneNumber;
    String targetAccountPhoneNumber;
    String emailAddress;
    String targetAccountEmailAddress;
    String currency;
    String targetAccountCurrency;
    double balance;
    double amount;
    String reference;
    String accountNumber;
    String targetAccountAccountNumber;
    String sortCode;
    String targetAccountSortCode;
    String startDate;
    String endDate;
    PaymentType Transfer;
    PaymentType Refund;
    PaymentType Deposit;
    PaymentType Withdraw;

    @InjectMocks
    private TransactionServiceImpl transactionService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setUpData() {
        id = UUID.randomUUID().toString();
        sourceAccountId = UUID.randomUUID().toString();
        targetAccountId = UUID.randomUUID().toString();
        name = "Martin";
        targetAccountFirstName = "James";
        surname = "King";
        targetAccountSurname = "Anderson";
        bankName = "NatWest";
        phoneNumber = "+4473458999125";
        targetAccountPhoneNumber = "+4479634177928";
        emailAddress = "current@fisglobal.com";
        targetAccountEmailAddress = "target@fisglobal.com";
        currency = "GBP";
        targetAccountCurrency = "GBP";
        balance = 500.00;
        amount = 20.00;
        reference = "unit-test";
        accountNumber = CodeUtils.getNewAccountNumber();
        targetAccountAccountNumber = CodeUtils.getNewAccountNumber();
        sortCode = CodeUtils.getNewSortCode();
        targetAccountSortCode = CodeUtils.getNewSortCode();
        startDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        endDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        Transfer = PaymentType.TRANSFER;
        Refund = PaymentType.REFUND;
        Deposit = PaymentType.DEPOSIT;
        Withdraw = PaymentType.WITHDRAW;
        currentAccount = new Account(sourceAccountId, name, bankName, phoneNumber, emailAddress, accountNumber, sortCode, balance, Status.ACTIVATE, new ArrayList<>());
        targetAccount = new Account(targetAccountId, name, bankName, phoneNumber, emailAddress, accountNumber, sortCode, balance, Status.ACTIVATE, new ArrayList<>());
    }

    @Test
    public void postTransfer() {
        Transaction transactionExpected = new Transaction(id, sourceAccountId, targetAccountId, targetAccountFirstName, amount, "", startDate, endDate, reference, Transfer);
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.of(currentAccount));
        when(accountRepository.findById(targetAccountId)).thenReturn(Optional.of(targetAccount));
        when(transactionRepository.save(transactionExpected)).thenReturn(transactionExpected);
        Transaction transactionActual = transactionService.processTransaction(id, sourceAccountId, targetAccountId, amount, reference, Transfer, null);
        verify(transactionRepository).save(transactionExpected);
        verify(transactionRepository).save(transactionActual);
        verify(accountRepository).findById(sourceAccountId);
        verify(accountRepository).findById(targetAccountId);
        assertEquals(transactionExpected, transactionActual);
    }

    @Test
    public void postTransferWhenTargetAccountMissing() {
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.of(currentAccount));
        when(accountRepository.findById(targetAccountId)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> transactionService.processTransaction(id, sourceAccountId, targetAccountId, amount, reference, Transfer, null));
        verify(accountRepository).findById(sourceAccountId);
        verify(accountRepository).findById(targetAccountId);
    }

    @Test
    public void postTransferWhenCurrentAccountMissing() {
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> transactionService.processTransaction(id, sourceAccountId, targetAccountId, amount, reference, Transfer, null));
        verify(accountRepository).findById(sourceAccountId);
    }

    @Test
    public void getTransactionId() {
        Transaction transaction = new Transaction(id, sourceAccountId, targetAccountId, targetAccountFirstName, amount, "", startDate, endDate, reference, Transfer);
        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));
        Transaction transactionActual = transactionService.getTransactionById(id);
        verify(transactionRepository).findById(id);
        assertEquals(transaction, transactionActual);
    }

    @Test
    public void getTransactionIdMissing() {
        when(transactionRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionById(id));
        verify(transactionRepository).findById(id);
    }

    @Test
    public void postDeposit() {
        Transaction transaction = new Transaction(id, sourceAccountId, targetAccountId, targetAccountFirstName, amount, "", startDate, endDate, reference, Deposit);
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.of(currentAccount));
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        Transaction transactionActual = transactionService.processTransaction(id, sourceAccountId, null, amount, reference, Deposit, null);
        verify(transactionRepository).save(transaction);
        verify(transactionRepository).save(transactionActual);
        verify(accountRepository).findById(sourceAccountId);
        assertEquals(transaction, transactionActual);
    }

    @Test
    public void postDepositWhenCurrentAccountMissing() {
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> transactionService.processTransaction(id, sourceAccountId, null, amount, reference, Deposit, null));
        verify(accountRepository).findById(sourceAccountId);
    }

    @Test
    public void postWithdrawal() {
        Transaction transactionExpected = new Transaction(id, sourceAccountId, null, targetAccountFirstName, amount, "", startDate, endDate, reference, Withdraw);
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.of(currentAccount));
        when(transactionRepository.save(transactionExpected)).thenReturn(transactionExpected);
        Transaction transactionActual = transactionService.processTransaction(id, sourceAccountId, null, amount, reference, Withdraw, null);
        verify(transactionRepository).save(transactionExpected);
        verify(transactionRepository).save(transactionActual);
        verify(accountRepository).findById(sourceAccountId);
        assertEquals(transactionExpected, transactionActual);
    }

    @Test
    public void postWithdrawWhenCurrentAccountMissing() {
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> transactionService.processTransaction(id, sourceAccountId, null, amount, reference, Withdraw, null));
        verify(accountRepository).findById(sourceAccountId);
    }

    @Test
    public void postRefund() {
        Transaction transactionExpected = new Transaction(id, sourceAccountId, targetAccountId, targetAccountFirstName, amount, "", startDate, endDate, reference, Refund);
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.of(currentAccount));
        when(accountRepository.findById(targetAccountId)).thenReturn(Optional.of(targetAccount));
        when(transactionRepository.save(transactionExpected)).thenReturn(transactionExpected);
        Transaction transactionActual = transactionService.processTransaction(id, sourceAccountId, targetAccountId, amount, reference, Refund, transactionExpected);
        verify(transactionRepository).save(transactionExpected);
        verify(transactionRepository).save(transactionActual);
        verify(accountRepository).findById(sourceAccountId);
        verify(accountRepository).findById(targetAccountId);
        assertEquals(transactionExpected, transactionActual);
    }

    @Test
    public void postRefundWhenTargetAccountMissing() {
        Transaction transaction = new Transaction(id, sourceAccountId, targetAccountId, targetAccountFirstName, amount, "", startDate, endDate, reference, Refund);
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.of(currentAccount));
        when(accountRepository.findById(targetAccountId)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> transactionService.processTransaction(id, sourceAccountId, targetAccountId, amount, reference, Refund, transaction));
        verify(accountRepository).findById(sourceAccountId);
        verify(accountRepository).findById(targetAccountId);
    }

    @Test
    public void postRefundWhenCurrentAccountMissing() {
        Transaction transaction = new Transaction(id, sourceAccountId, targetAccountId, targetAccountFirstName, amount, "", startDate, endDate, reference, Refund);
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> transactionService.processTransaction(id, sourceAccountId, targetAccountId, amount, reference, Refund, transaction));
        verify(accountRepository).findById(sourceAccountId);
    }
}
