package project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import project.enums.Status;
import project.dto.account.AccountCriteria;
import project.repository.entity.Account;
import project.repository.entity.Transaction;
import project.dto.account.CreateAccountRequest;
import project.service.impl.AccountServiceImpl;
import project.utility.CodeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static project.utility.CommonUtils.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@WebMvcTest(AccountController.class)
public class AccountRestControllerTest {

    @MockBean
    private AccountServiceImpl accountServiceImpl;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postAccount() throws Exception {
        String id = UUID.randomUUID().toString();
        String name = "Martin";
        String bank = "NatWest";
        String phoneNumber = "+447587155942";
        String emailAddress = "william.cuthbert@fisglobal.com";
        String accountNo = CodeUtils.getNewAccountNumber();
        String sortCode = CodeUtils.getNewSortCode();
        double balance = 0;
        List<Transaction> transactions = new ArrayList<>();

        Account account = new Account(id, name, bank, phoneNumber, emailAddress, accountNo,
            sortCode, balance, Status.ACTIVATE, transactions);
        CreateAccountRequest createAccountRequest = new CreateAccountRequest(bank, name, phoneNumber, emailAddress);
        when(accountServiceImpl.createAccount(createAccountRequest)).thenReturn(account);
        mockMvc.perform(MockMvcRequestBuilders.post(ACCOUNT_ENDPOINT)
                        .content(new ObjectMapper().writeValueAsString(createAccountRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.fullName", is(name)))
                .andExpect(jsonPath("$.bankName", is(bank)))
                .andExpect(jsonPath("$.phoneNumber", is(phoneNumber)))
                .andExpect(jsonPath("$.emailAddress", is(emailAddress)))
                .andExpect(jsonPath("$.sortCode", is(sortCode)))
                .andExpect(jsonPath("$.accountNumber", is(accountNo)))
                .andExpect(jsonPath("$.balance", is(balance)))
                .andExpect(jsonPath("$.transactions", is(transactions)))
                .andReturn().getResponse().getContentAsString();
        verify(accountServiceImpl).createAccount(createAccountRequest);
    }

    @Test
    void getAccountBySortCodeAndAccountNumber() throws Exception {
        String id = UUID.randomUUID().toString();
        String name = "Martin";
        String bank = "NatWest";
        String phoneNumber = "+447587155942";
        String emailAddress = "william.cuthbert@fisglobal.com";
        String accountNumber = CodeUtils.getNewAccountNumber();
        String sortCode = CodeUtils.getNewSortCode();
        double balance = 0;
        List<Transaction> transactions = new ArrayList<>();

        Account account = new Account(id, name, bank, phoneNumber, emailAddress, accountNumber,
            sortCode, balance, Status.ACTIVATE, transactions);

        when(accountServiceImpl.getAccountBySortCodeAndAccountNumber(sortCode, accountNumber)).thenReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.get(ACCOUNT_ENDPOINT)
                        .param(SORT_CODE, sortCode)
                        .param(ACCOUNT_NUMBER, accountNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.id", is(id)))
            .andExpect(jsonPath("$.fullName", is(name)))
            .andExpect(jsonPath("$.bankName", is(bank)))
            .andExpect(jsonPath("$.phoneNumber", is(phoneNumber)))
            .andExpect(jsonPath("$.emailAddress", is(emailAddress)))
            .andExpect(jsonPath("$.sortCode", is(sortCode)))
            .andExpect(jsonPath("$.accountNumber", is(accountNumber)))
            .andExpect(jsonPath("$.balance", is(balance)))
            .andExpect(jsonPath("$.transactions", is(transactions)))
                .andReturn();

        verify(accountServiceImpl).getAccountBySortCodeAndAccountNumber(sortCode, accountNumber);
    }

    @Test
    void getAccounts() throws Exception {
        String id = UUID.randomUUID().toString();
        String name = "Martin";
        String bank = "NatWest";
        String phoneNumber = "+447587155942";
        String emailAddress = "william.cuthbert@fisglobal.com";
        String accountNo = CodeUtils.getNewAccountNumber();
        String sortCode = CodeUtils.getNewSortCode();
        double balance = 0;
        List<Transaction> transactions = new ArrayList<>();

        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account(id, name, bank, phoneNumber, emailAddress, accountNo,
            sortCode, balance, Status.ACTIVATE, transactions));
        accounts.add(new Account(id, name, bank, phoneNumber, emailAddress, accountNo,
            sortCode, balance, Status.ACTIVATE, transactions));
        AccountCriteria accountCriteria = new AccountCriteria();
        accountCriteria.setAccountId(id);

        when(accountServiceImpl.finalAllAccounts(accountCriteria)).thenReturn(accounts);

        mockMvc.perform(MockMvcRequestBuilders.get(ACCOUNTS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accounts[0].id", is(id)))
                .andExpect(jsonPath("$.accounts[0].bankName", is(bank)))
                .andExpect(jsonPath("$.accounts[0].fullName", is(name)))
                .andExpect(jsonPath("$.accounts[0].phoneNumber", is(phoneNumber)))
                .andExpect(jsonPath("$.accounts[0].emailAddress", is(emailAddress)))
                .andExpect(jsonPath("$.accounts[0].sortCode", is(sortCode)))
                .andExpect(jsonPath("$.accounts[0].accountNumber", is(accountNo)))
                .andExpect(jsonPath("$.accounts[0].balance", is(balance)))
                .andExpect(jsonPath("$.accounts[0].transactions", is(transactions)))
                .andExpect(jsonPath("$.accounts[1].id", is(id)))
                .andExpect(jsonPath("$.accounts[1].bankName", is(bank)))
                .andExpect(jsonPath("$.accounts[1].fullName", is(name)))
                .andExpect(jsonPath("$.accounts[1].phoneNumber", is(phoneNumber)))
                .andExpect(jsonPath("$.accounts[1].emailAddress", is(emailAddress)))
                .andExpect(jsonPath("$.accounts[1].sortCode", is(sortCode)))
                .andExpect(jsonPath("$.accounts[1].accountNumber", is(accountNo)))
                .andExpect(jsonPath("$.accounts[1].balance", is(balance)))
                .andExpect(jsonPath("$.accounts[1].transactions", is(transactions)))
                .andReturn();

        verify(accountServiceImpl).finalAllAccounts(accountCriteria);
    }

    @Test
    public void deleteAccount() throws Exception {
        String id = UUID.randomUUID().toString();
        String name = "Martin";
        String bank = "NatWest";
        String phoneNumber = "+447587155942";
        String emailAddress = "william.cuthbert@fisglobal.com";
        String accountNo = CodeUtils.getNewAccountNumber();
        String sortCode = CodeUtils.getNewSortCode();
        double balance = 0;
        List<Transaction> transactions = new ArrayList<>();

        Account account = new Account(id, name, bank, phoneNumber, emailAddress, accountNo,
            sortCode, balance, Status.ACTIVATE, transactions);

        mockMvc.perform(MockMvcRequestBuilders.delete(ACCOUNT_ID_ENDPOINT, id))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NO_CONTENT.value()))
                .andReturn();

        verify(accountServiceImpl).deleteAccount(account, id);
    }

//    @Test
//    public void updateAccount() throws Exception {
//        String id = UUID.randomUUID().toString();
//        String name = "Martin";
//        String surname = "King";
//        String bank = "NatWest";
//        String phoneNumber = "+447587155942";
//        String emailAddress = "william.cuthbert@fisglobal.com";
//        String customerNumber = CodeUtils.generateCustomerNumber();
//        String passNumber = CodeUtils.generatePassNumber();
//        String verificationCode = CodeUtils.generateVerificationCode();
//        String currency = "GBP";
//        String accountNo = CodeUtils.generateAccountNumber();
//        String sortCode = CodeUtils.generateSortCode();
//        double balance = 0;
//        List<Transaction> transactions = new ArrayList<>();
//        LoginStatus loginStatus = LoginStatus.LOGIN_ACCESS_GRANTED;
//
//        Account account = new Account(id, name, surname, bank, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNo, sortCode, balance, currency, transactions, loginStatus);
//        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest(phoneNumber, emailAddress);
//        when(accountServiceImpl.getAccountById(id)).thenReturn(account);
//        when(accountServiceImpl.updateAccount(account)).thenReturn(account);
//        mockMvc.perform(MockMvcRequestBuilders.put(ACCOUNT_ID_ENDPOINT, id)
//                        .content(new ObjectMapper().writeValueAsString(updateAccountRequest))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(jsonPath("$.id", is(id)))
//                .andExpect(jsonPath("$.firstName", is(name)))
//                .andExpect(jsonPath("$.surname", is(surname)))
//                .andExpect(jsonPath("$.bankName", is(bank)))
//                .andExpect(jsonPath("$.phoneNumber", is(phoneNumber)))
//                .andExpect(jsonPath("$.emailAddress", is(emailAddress)))
//                .andExpect(jsonPath("$.customerNumber", is(customerNumber)))
//                .andExpect(jsonPath("$.passNumber", is(passNumber)))
//                .andExpect(jsonPath("$.verificationCode", is(verificationCode)))
//                .andExpect(jsonPath("$.sortCode", is(sortCode)))
//                .andExpect(jsonPath("$.accountNumber", is(accountNo)))
//                .andExpect(jsonPath("$.balance", is(balance)))
//                .andExpect(jsonPath("$.currency", is(currency)))
//                .andExpect(jsonPath("$.transactions", is(transactions)))
//                .andReturn();
//        verify(accountServiceImpl).updateAccount(account);
//    }
}