package com.project.controller;

import static com.project.utility.ApiUtils.ACCOUNTS_ENDPOINT;
import static com.project.utility.ApiUtils.ACCOUNT_ENDPOINT;
import static com.project.utility.ApiUtils.ACCOUNT_ID_ENDPOINT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dto.account.AccountDtoRequest;
import com.project.enums.Status;
import com.project.repository.AccountRepository;
import com.project.repository.TransactionRepository;
import com.project.service.TransactionService;
import com.project.service.mapper.AccountMapper;
import com.project.dto.account.AccountCriteria;
import com.project.repository.entity.Account;
import com.project.service.impl.AccountServiceImpl;

import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

@MockitoSettings(strictness = Strictness.LENIENT)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @MockBean
    private AccountServiceImpl accountServiceImpl;

    @MockBean
    private AccountMapper accountMapper;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void postAccount() throws Exception {
        String id = UUID.randomUUID().toString();
        String name = "Martin";
        String bank = "NatWest";
        String phoneNumber = "+447587155942";
        String emailAddress = "william.cuthbert@fisglobal.com";

        Account account = new Account();
        account.setId(id);
        account.setFullName(name);
        account.setBankName(bank);
        account.setPhoneNumber(phoneNumber);
        account.setEmailAddress(emailAddress);

        AccountDtoRequest createAccountRequest = new AccountDtoRequest(bank, name, phoneNumber, emailAddress);
        when(accountServiceImpl.createAccount(createAccountRequest)).thenReturn(account);
        mockMvc.perform(post("/v1/api" + ACCOUNT_ENDPOINT)
                .content(objectMapper.writeValueAsString(createAccountRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
        verify(accountServiceImpl).createAccount(createAccountRequest);
    }

    @Test
    void getAccounts() throws Exception {
        String id = UUID.randomUUID().toString();
        Account accountA = new Account();
        accountA.setId(id);
        accountA.setStatus(Status.ACTIVATE);
        List<Account> accounts = Collections.singletonList(accountA);

        AccountCriteria accountCriteria = AccountCriteria.builder()
            .accountId(id)
            .build();

        when(accountServiceImpl.findAllAccounts(accountCriteria)).thenReturn(accounts);

        mockMvc.perform(get("/v1/api" + ACCOUNTS_ENDPOINT)
                .content(objectMapper.writeValueAsString(accountCriteria))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk());
        verify(accountServiceImpl).findAllAccounts(accountCriteria);
    }

    @Test
    public void deleteAccount() throws Exception {
        String id = UUID.randomUUID().toString();
        Account account = new Account();
        account.setId(id);
        when(accountServiceImpl.getAccountIdWithoutStatusCheck(id)).thenReturn(account);
        when(accountServiceImpl.deleteAccount(account, id)).thenReturn(account);
        mockMvc.perform(delete("/v1/api" + ACCOUNT_ID_ENDPOINT, id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent());
        verify(accountServiceImpl).deleteAccount(account, id);
    }
}