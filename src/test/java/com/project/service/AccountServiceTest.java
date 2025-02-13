package com.project.service;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import com.project.enums.Status;
import com.project.dto.account.AccountCriteria;
import com.project.dto.account.AccountDtoRequest;
import com.project.repository.entity.Account;
import com.project.repository.AccountRepository;
import com.project.repository.TransactionRepository;
import com.project.service.impl.AccountServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

  @InjectMocks
  private AccountServiceImpl accountServiceImpl;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private TransactionRepository transactionRepository;

  @Test
  public void createAccount() {
    String id = UUID.randomUUID().toString();
    String emailAddress = "email";
    String phoneNumber = "+447587155942";
    String bank = "bank";
    String name = "test";

    Account accountExpected = new Account();
    accountExpected.setId(id);
    accountExpected.setBankName(bank);
    accountExpected.setEmailAddress(emailAddress);
    accountExpected.setPhoneNumber(phoneNumber);
    accountExpected.setFullName(name);

    when(accountRepository.findByEmailAddressOrPhoneNumber(emailAddress, phoneNumber)).thenReturn(Optional.empty());
    when(accountRepository.save(any(Account.class))).thenReturn(accountExpected);
    AccountDtoRequest createAccountRequest = new AccountDtoRequest();
    createAccountRequest.setBankName(bank);
    createAccountRequest.setFirstName(name);
    createAccountRequest.setPhoneNumber(phoneNumber);
    createAccountRequest.setEmailAddress(emailAddress);
    Account accountActual = accountServiceImpl.createAccount(createAccountRequest);
    verify(accountRepository).findByEmailAddressOrPhoneNumber(emailAddress, phoneNumber);
    verify(accountRepository).save(any(Account.class));
    assertEquals(accountExpected, accountActual);
  }

  @Test
  public void findAllAccounts() {
    String id = UUID.randomUUID().toString();
    Account accountA = new Account();
    accountA.setId(id);
    Account accountB = new Account();
    accountB.setId(id);
    List<Account> accountsExpected = Arrays.asList(accountA, accountB);
    when(accountRepository.findByStatus(Status.ACTIVATE)).thenReturn(accountsExpected);
    List<Account> accountsActual = accountServiceImpl.findAllAccounts(new AccountCriteria());
    verify(accountRepository).findByStatus(Status.ACTIVATE);
    assertEquals(accountsExpected, accountsActual);
  }

  @Test
  public void deleteAccount() {
    String id = UUID.randomUUID().toString();
    Account accountExpected = new Account();
    accountExpected.setId(id);
    when(accountRepository.findById(id)).thenReturn(Optional.of(accountExpected));
    accountServiceImpl.deleteAccount(accountExpected, id);
    verify(accountRepository).save(accountExpected);
  }

  @Test
  public void updateAccount() {
    String id = UUID.randomUUID().toString();
    String newPhoneNumber = "+447911894584";
    String newEmailAddress = "testnew@gmail.com";
    Account accountToSave = new Account();
    accountToSave.setId(id);
    accountToSave.setPhoneNumber(newPhoneNumber);
    accountToSave.setEmailAddress(newEmailAddress);

    String existingPhoneNumber = "+447587155942";
    String existingEmailAddress = "william.cuthbert@fisglobal.com";
    Account accountExisting = new Account();
    accountExisting.setId(id);
    accountExisting.setEmailAddress(existingEmailAddress);
    accountExisting.setPhoneNumber(existingPhoneNumber);

    when(accountRepository.findById(id)).thenReturn(Optional.of(accountExisting));
    when(accountRepository.save(accountToSave)).thenReturn(accountToSave);
    accountServiceImpl.updateAccount(accountToSave);
    verify(accountRepository).findById(id);
    verify(accountRepository).save(accountToSave);
  }
}