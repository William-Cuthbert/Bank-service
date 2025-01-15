//package com.project.service;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.mockito.quality.Strictness;
//import com.project.enums.Status;
//import com.project.errorhandler.exception.AccountNotFoundException;
//import com.project.dto.account.AccountCriteria;
//import com.project.dto.account.CreateAccountRequest;
//import com.project.repository.entity.Account;
//import com.project.repository.entity.Transaction;
//import com.project.repository.AccountRepository;
//import com.project.repository.TransactionRepository;
//import com.project.service.impl.AccountServiceImpl;
//import com.project.utility.CodeUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@MockitoSettings(strictness = Strictness.LENIENT)
//@ExtendWith(MockitoExtension.class)
//public class AccountServiceTest {
//    @InjectMocks
//    private AccountServiceImpl accountServiceImpl;
//    @Mock
//    private AccountRepository accountRepository;
//    @Mock
//    private TransactionRepository transactionRepository;
//    @Test
//    public void createAccount() {
//        String id = UUID.randomUUID().toString();
//        String name = "Martin";
//        String bank = "NatWest";
//        String phoneNumber = "+447587155942";
//        String emailAddress = "william.cuthbert@fisglobal.com";
//        String accountNo = CodeUtils.getNewAccountNumber();
//        String sortCode = CodeUtils.getNewSortCode();
//        double balance = 0;
//        List<Transaction> transactions = new ArrayList<>();
//        Account accountExpected = new Account(id, name, bank, phoneNumber, emailAddress, accountNo, sortCode, balance, Status.ACTIVATE, transactions);
//
//        when(accountRepository.findBySortCodeAndAccountNumber(sortCode, accountNo)).thenReturn(Optional.empty());
//        when(accountRepository.save(accountExpected)).thenReturn(accountExpected);
//        Account accountActual = accountServiceImpl.createAccount(new CreateAccountRequest(bank,name,phoneNumber,emailAddress));
//        verify(accountRepository).findBySortCodeAndAccountNumber(sortCode, accountNo);
//        verify(accountRepository).save(accountExpected);
//        assertEquals(accountExpected, accountActual);
//    }
//
//    @Test
//    public void getAccountBySortCodeAndAccountNumber() {
//        String id = UUID.randomUUID().toString();
//        String name = "Martin";
//        String bankName = "NatWest";
//        String phoneNumber = "+447587155942";
//        String emailAddress = "william.cuthbert@fisglobal.com";
//        String accountNumber = CodeUtils.getNewAccountNumber();
//        String sortCode = CodeUtils.getNewSortCode();
//        double balance = 0;
//        List<Transaction> transactions = new ArrayList<>();
//        Account accountExpected = new Account(id, name, bankName, phoneNumber, emailAddress, accountNumber, sortCode, balance, Status.ACTIVATE, transactions);
//
//        when(accountRepository.findBySortCodeAndAccountNumber(sortCode, accountNumber)).thenReturn(Optional.of(accountExpected));
//        Account accountActual = accountServiceImpl.getAccountBySortCodeAndAccountNumber(sortCode, accountNumber);
//        verify(accountRepository).findBySortCodeAndAccountNumber(sortCode, accountNumber);
//        assertEquals(accountExpected, accountActual);
//    }
//
//    @Test
//    public void getAccountBySortCodeAndAccountNumberMissing() {
//        String sortCode = CodeUtils.getNewSortCode();
//        String accountNumber = CodeUtils.getNewAccountNumber();
//        when(accountRepository.findBySortCodeAndAccountNumber(sortCode, accountNumber)).thenReturn(Optional.empty());
//        assertThrows(AccountNotFoundException.class, () -> accountServiceImpl.getAccountBySortCodeAndAccountNumber(sortCode, accountNumber));
//        verify(accountRepository).findBySortCodeAndAccountNumber(sortCode, accountNumber);
//    }
//
//    @Test
//    public void finalAllAccounts() {
//        String id = UUID.randomUUID().toString();
//        String name = "Martin";
//        String bank = "NatWest";
//        String phoneNumber = "+447587155942";
//        String emailAddress = "william.cuthbert@fisglobal.com";
//        String accountNumber = CodeUtils.getNewAccountNumber();
//        String sortCode = CodeUtils.getNewSortCode();
//        double balance = 0;
//        List<Transaction> transactions = new ArrayList<>();
//
//        List<Account> accountsExpected = new ArrayList<>();
//        accountsExpected.add(new Account(id, name, bank, phoneNumber, emailAddress, accountNumber, sortCode, balance, Status.ACTIVATE, transactions));
//        accountsExpected.add(new Account(id, name, bank, phoneNumber, emailAddress, accountNumber, sortCode, balance, Status.ACTIVATE, transactions));
//
//        when(accountRepository.findByStatus(Status.ACTIVATE)).thenReturn(accountsExpected);
//        List<Account> accountsActual = accountServiceImpl.finalAllAccounts(new AccountCriteria());
//        verify(accountRepository).findByStatus(Status.ACTIVATE);
//        assertEquals(accountsExpected, accountsActual);
//    }
//
//    @Test
//    public void deleteAccount() {
//        String id = UUID.randomUUID().toString();
//        String name = "Martin";
//        String bank = "NatWest";
//        String phoneNumber = "+447587155942";
//        String emailAddress = "william.cuthbert@fisglobal.com";
//        String accountNo = CodeUtils.getNewAccountNumber();
//        String sortCode = CodeUtils.getNewSortCode();
//        double balance = 0;
//        List<Transaction> transactions = new ArrayList<>();
//        Account accountExpected = new Account(id, name, bank, phoneNumber, emailAddress, accountNo, sortCode, balance, Status.ACTIVATE, transactions);
//
//        when(accountRepository.findById(id)).thenReturn(Optional.of(accountExpected));
//        accountServiceImpl.deleteAccount(accountExpected,id);
//        verify(accountRepository).deleteById(id);
//    }
//
//    @Test
//    public void updateAccount() {
//        String id = UUID.randomUUID().toString();
//        String name = "Martin";
//        String bank = "NatWest";
//        String accountNo = CodeUtils.getNewAccountNumber();
//        String sortCode = CodeUtils.getNewSortCode();
//        double balance = 0;
//        List<Transaction> transactions = new ArrayList<>();
//
//        String newPhoneNumber = "+447911894584";
//        String newEmailAddress = "testnew@gmail.com";
//        Account accountToSave = new Account(id, name, bank, newPhoneNumber, newEmailAddress, accountNo, sortCode, balance, Status.ACTIVATE, transactions);
//
//        String existingPhoneNumber = "+447587155942";
//        String existingEmailAddress = "william.cuthbert@fisglobal.com";
//        Account accountExisting = new Account(id, name, bank, existingPhoneNumber, existingEmailAddress, accountNo, sortCode, balance, Status.ACTIVATE, transactions);
//
//        when(accountRepository.findById(id)).thenReturn(Optional.of(accountExisting));
//        when(accountRepository.save(accountToSave)).thenReturn(accountToSave);
//        accountServiceImpl.updateAccount(accountToSave);
//        verify(accountRepository).findById(id);
//        verify(accountRepository).save(accountToSave);
//    }
//}
