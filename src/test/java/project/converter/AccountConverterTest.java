package project.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.enums.Status;
import project.model.UpdateAccountRequest;
import project.model.AccountsResponse;
import project.repository.entity.Account;
import project.repository.entity.Transaction;
import project.model.AccountResponse;
import project.utility.CodeUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountConverterTest {

    List<Account> accountList;
    private Account account;
    private AccountResponse accountResponse;
    private AccountsResponse accountsResponse;
    private UpdateAccountRequest updateAccountRequest;

    // todo: create account request

    @BeforeEach
    public void setup() {
        String id = UUID.randomUUID().toString();
        String name = "Martin";
        String bank = "NatWest";
        String phoneNumber = "+447587155942";
        String emailAddress = "william.cuthbert@fisglobal.com";
        String accountNo = CodeUtils.getNewAccountNumber();
        String sortCode = CodeUtils.getNewSortCode();
        double balance = 0;
        List<Transaction> transactions = new ArrayList<>();
        accountList = new ArrayList<>();
        account = new Account(id, name, bank, phoneNumber, emailAddress, accountNo, sortCode, balance, Status.ACTIVATE, transactions);
        accountResponse = new AccountResponse(id, name, bank, phoneNumber, emailAddress, accountNo, sortCode, balance, transactions);
        accountsResponse = new AccountsResponse(accountList);
        updateAccountRequest = new UpdateAccountRequest(phoneNumber, emailAddress);
    }

    @Test
    void covertAccountToAccountResponse() {
        assertEquals(accountResponse, AccountConverter.convertToAccountResponse(account));
    }

    @Test
    void convertAccountsToAccountsResponse() {
        assertEquals(accountsResponse, AccountConverter.convertAccountsToAccountsResponse(accountList));
    }

    @Test
    void covertAccountResponseToAccount() {
        assertEquals(account, AccountConverter.covertAccountResponseToAccount(accountResponse));
    }

    @Test
    void convertAccountRequestToAccount() {
        assertEquals(account, AccountConverter.convertAccountRequestToAccount(account.getId(), updateAccountRequest, account));
    }
}
