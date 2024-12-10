package project.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.enums.PaymentType;
import project.repository.entity.Transaction;
import project.model.DepositRequest;
import project.model.RefundRequest;
import project.model.TransferRequest;
import project.model.WithdrawRequest;
import project.model.TransactionResponse;
import project.utility.CodeUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionConverterTest {

    private Transaction transaction;
    private TransactionResponse transactionResponse;
    private TransferRequest transferRequest;
    private RefundRequest refundRequest;
    private DepositRequest depositRequest;
    private WithdrawRequest withdrawRequest;

    @BeforeEach
    public void setup() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String id = UUID.randomUUID().toString();
        String sourceAccountId = UUID.randomUUID().toString();
        String targetAccountId = UUID.randomUUID().toString();
        String name = "Martin";
        String surname = "King";
        double amount = 20.00;
        String reference = "unit test";
        String accountNumber = CodeUtils.getNewAccountNumber();
        String sortCode = CodeUtils.getNewSortCode();
        transaction = new Transaction(id, sourceAccountId, targetAccountId, name, surname, amount,
                LocalDateTime.now().format(dateTimeFormatter), LocalDateTime.now().format(dateTimeFormatter), reference, PaymentType.TRANSFER);
        transactionResponse = new TransactionResponse(id, sourceAccountId, targetAccountId, name, surname, amount,
                LocalDateTime.now().format(dateTimeFormatter), LocalDateTime.now().format(dateTimeFormatter), reference, PaymentType.TRANSFER);
        transferRequest = new TransferRequest(amount, reference);
        refundRequest = new RefundRequest(id, amount);
        depositRequest = new DepositRequest(accountNumber, amount);
        withdrawRequest = new WithdrawRequest(sortCode, accountNumber, amount);
    }

    @Test
    public void convertTransactionToTransactionResponse() {
        assertEquals(transactionResponse, TransactionConverter.convertToTransactionResponse(transaction));
    }

    @Test
    public void convertTransactionResponseToTransaction() {
        assertEquals(transaction, TransactionConverter.convertTransactionResponseToTransaction(transactionResponse));
    }
}
