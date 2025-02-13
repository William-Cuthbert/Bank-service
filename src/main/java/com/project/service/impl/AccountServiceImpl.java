package com.project.service.impl;

import static com.project.utility.CodeUtils.getNewAccountNumber;
import static com.project.utility.CodeUtils.getNewSortCode;

import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.enums.Status;
import com.project.errorhandler.exception.AccountExistsException;
import com.project.errorhandler.exception.AccountNotFoundException;
import com.project.dto.account.AccountCriteria;
import com.project.dto.account.AccountDtoRequest;
import com.project.repository.entity.Account;
import com.project.repository.AccountRepository;
import com.project.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;
import com.project.service.AccountService;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private static final double ZERO = 0;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Account createAccount(final AccountDtoRequest createAccountRequest) {
        doesAccountExists(createAccountRequest.getEmailAddress(),
            createAccountRequest.getPhoneNumber());
        log.info("Account does not exist, proceeding ahead to creating a new bank account");
        Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setFullName(createAccountRequest.getFirstName());
        account.setEmailAddress(createAccountRequest.getEmailAddress());
        account.setBankName(createAccountRequest.getBankName());
        account.setSortCode(getNewSortCode());
        account.setAccountNumber(getNewAccountNumber());
        account.setBalance(ZERO);
        account = accountRepository.save(account);
        log.info("Account created... accountId={}", account.getId());
        return account;
    }

//    @Override
//    public Account getAccountBySortCodeAndAccountNumber(final String sortCode, final String accountNumber) {
//        log.info("Getting account... sortCode={} accountNumber={}", sortCode, accountNumber);
//        Account account = accountRepository.findBySortCodeAndAccountNumber(sortCode, accountNumber)
//            .orElseThrow(() -> new AccountNotFoundException(
//                "Account with sort code=" + sortCode + " and account number=" + accountNumber
//                    + " could not be found"));
//        account.setTransactions(
//            transactionRepository.findBySourceAccountIdOrderByInitiationDate(account.getId()));
//        return account;
//    }

    @Override
    public List<Account> findAllAccounts(AccountCriteria accountCriteria) {
        log.info("Enter getAccounts method");
        List<Account> accounts = accountRepository.findByStatus(Status.ACTIVATE)
            .stream()
            .filter(isAccountIdCriteriaMeets(accountCriteria))
            .filter(isAccountFullNameCriteriaMeets(accountCriteria))
            .filter(isAccountEmailAddressCriteriaMeets(accountCriteria))
            .filter(isAccountBankNameCriteriaMeets(accountCriteria))
            .filter(isAccountSortCodeCriteriaMeets(accountCriteria))
            .filter(isAccountNumberCriteriaMeets(accountCriteria))
            .filter(isAccountPhoneNumberCriteriaMeets(accountCriteria))
            .collect(Collectors.toList());
        log.info("Collecting all transactions for all filtered account(s)");
        accounts.forEach(account ->
            account.setTransactions(
                transactionRepository.findBySourceAccountIdOrderByInitiationDate(
                    account.getId())));
        return accounts;
    }

    @Override
    public Account updateAccount(final Account account) {
        log.info("Entering updateAccount method of AccountServiceImpl");
        Account updatedAccount = null;
        if (accountRepository.findById(account.getId()).isPresent()) {
            updatedAccount = accountRepository.save(account);
        }
        return updatedAccount;
    }

    @Override
    public Account deleteAccount(final Account account, final String id) {
        log.info("Entering deleteAccount method of AccountServiceImpl");
        if (account.getStatus() == Status.DEACTIVATE) {
            throw new AccountNotFoundException(id);
        }
        account.setStatus(Status.DEACTIVATE);
        accountRepository.save(account);
        log.info("Existing deleteAccount method of AccountServiceImpl");
        return account;
    }

    @Override
    public Account getAccountIdWithoutStatusCheck(String currentAccountId) {
        Optional<Account> account = accountRepository.findById(currentAccountId);
        if (!account.isPresent()) {
            throw new AccountNotFoundException(currentAccountId);
        }
        return account.get();
    }

    @Override
    public Account getAccount(String accountId) {
        return accountRepository.findById(accountId)
            .orElseThrow(() -> {
                log.info("Account cannot be found");
                return new AccountNotFoundException("Account not found: " + accountId);
            });
    }

    private void doesAccountExists(String email, String phone) {
        log.info("Checking for existing account, email={} phone number={}", email, phone);
        Optional<Account> existingAccount = accountRepository.findByEmailAddressOrPhoneNumber(email,
            phone);
        if (existingAccount.isPresent()) {
            throw new AccountExistsException(
                String.format("Account exists: email=%s, phone number=%s", email, phone));
        }
    }

    private Predicate<Account> isAccountIdCriteriaMeets(
        AccountCriteria criteria) {
        final boolean isAccountIdEmpty = criteria.getAccountId() == null;
        return account -> !isAccountIdEmpty ? criteria.getAccountId()
            .equals(account.getId()) : Boolean.TRUE;
    }

    private Predicate<Account> isAccountFullNameCriteriaMeets(
        AccountCriteria criteria) {
        final boolean isFullNameEmpty = criteria.getFullName() == null;
        return account -> !isFullNameEmpty ? criteria.getFullName()
            .equals(account.getFullName()) : Boolean.TRUE;
    }

    private Predicate<Account> isAccountEmailAddressCriteriaMeets(
        AccountCriteria criteria) {
        final boolean isEmailEmpty = criteria.getEmailAddress() == null;
        return account -> !isEmailEmpty ? criteria.getEmailAddress()
            .equals(account.getEmailAddress()) : Boolean.TRUE;
    }

    private Predicate<Account> isAccountBankNameCriteriaMeets(
        AccountCriteria criteria) {
        final boolean isAccountBankNameEmpty = criteria.getBankName() == null;
        return account -> !isAccountBankNameEmpty ? criteria.getBankName()
            .equals(account.getBankName()) : Boolean.TRUE;
    }

    private Predicate<Account> isAccountSortCodeCriteriaMeets(
        AccountCriteria criteria) {
        final boolean isSortCodeEmpty = criteria.getSortCode() == null;
        return account -> !isSortCodeEmpty ? criteria.getSortCode()
            .equals(account.getSortCode()) : Boolean.TRUE;
    }

    private Predicate<Account> isAccountNumberCriteriaMeets(
        AccountCriteria criteria) {
        final boolean isAccountNumberEmpty = criteria.getAccountNumber() == null;
        return account -> !isAccountNumberEmpty ? criteria.getAccountNumber()
            .equals(account.getAccountNumber()) : Boolean.TRUE;
    }

    private Predicate<Account> isAccountPhoneNumberCriteriaMeets(
        AccountCriteria criteria) {
        final boolean isAccountPhoneNumberEmpty = criteria.getPhoneNumber() == null;
        return account -> !isAccountPhoneNumberEmpty ? criteria.getPhoneNumber()
            .equals(account.getPhoneNumber()) : Boolean.TRUE;
    }
}
