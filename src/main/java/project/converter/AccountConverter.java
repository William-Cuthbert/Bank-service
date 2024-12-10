package project.converter;

import project.model.UpdateAccountRequest;
import project.model.AccountResponse;
import project.model.AccountsResponse;
import project.repository.entity.Account;

import java.util.List;

public class AccountConverter {
    public static AccountResponse convertToAccountResponse(Account account) {
        return new AccountResponse(account.getId(), account.getFullName(), account.getSurname(), account.getBankName(), account.getPhoneNumber(), account.getEmailAddress(), account.getCustomerNumber(), account.getPassNumber(), account.getVerificationCode(), account.getAccountNumber(), account.getSortCode(), account.getBalance(), account.getCurrency(), account.getTransactions(), account.getLoginStatus());
    }

    public static AccountsResponse convertAccountsToAccountsResponse(List<Account> accountList) {
        return new AccountsResponse(accountList);
    }

    public static Account covertAccountResponseToAccount(AccountResponse accountResponse) {
        return new Account(accountResponse.getId(), accountResponse.getFirstName(), accountResponse.getSurname(), accountResponse.getBankName(), accountResponse.getPhoneNumber(), accountResponse.getEmailAddress(), accountResponse.getCustomerNumber(), accountResponse.getPassNumber(), accountResponse.getVerificationCode(), accountResponse.getAccountNumber(), accountResponse.getSortCode(), accountResponse.getBalance(), accountResponse.getCurrency(), accountResponse.getTransactions(), accountResponse.getLoginStatus());
    }

    public static Account convertAccountRequestToAccount(String currentAccountId, UpdateAccountRequest updateAccountRequest, Account currentAccount) {
        return new Account(currentAccountId, currentAccount.getFullName(), currentAccount.getSurname(), currentAccount.getBankName(), updateAccountRequest.getPhoneNumber(), updateAccountRequest.getEmailAddress(), currentAccount.getCustomerNumber(), currentAccount.getPassNumber(), currentAccount.getVerificationCode(), currentAccount.getAccountNumber(), currentAccount.getSortCode(), currentAccount.getBalance(), currentAccount.getCurrency(), currentAccount.getTransactions(), currentAccount.getLoginStatus());
    }
}
