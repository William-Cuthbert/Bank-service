package com.error;

import com.project.errorhandler.exception.AccountExistsException;
import com.project.errorhandler.exception.AccountNotFoundException;
import com.project.errorhandler.exception.AccountVerificationFailureException;
import com.project.errorhandler.exception.InsufficientBalanceException;
import com.project.errorhandler.exception.TransactionNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import com.project.errorhandler.GlobalErrorHandler;
import com.project.dto.ErrorResponse;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class GlobalErrorHandlerTest {

    private final GlobalErrorHandler globalErrorHandler = new GlobalErrorHandler();

    @Test
    public void accountNotFoundExceptionTest() {
        WebRequest webRequest = mock(WebRequest.class);
        AccountNotFoundException exception = new AccountNotFoundException("Test message");

        ResponseEntity<Object> response = globalErrorHandler.accountNotFoundException(exception, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Test message", ((ErrorResponse) Objects.requireNonNull(response.getBody())).getMessage());
    }

    @Test
    public void accountExistsExceptionTest() {
        WebRequest webRequest = mock(WebRequest.class);
        AccountExistsException exception = new AccountExistsException("Test message");

        ResponseEntity<Object> response = globalErrorHandler.accountExistsException(exception, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Test message", ((ErrorResponse) Objects.requireNonNull(response.getBody())).getMessage());
    }

    @Test
    public void transactionNotFoundExceptionTest() {
        WebRequest webRequest = mock(WebRequest.class);
        TransactionNotFoundException exception = new TransactionNotFoundException("Test message");

        ResponseEntity<Object> response = globalErrorHandler.transactionNotFoundException(exception, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Test message", ((ErrorResponse) Objects.requireNonNull(response.getBody())).getMessage());
    }

    @Test
    public void accountVerificationFailureExceptionTest() {
        WebRequest webRequest = mock(WebRequest.class);
        AccountVerificationFailureException exception = new AccountVerificationFailureException("Test message");

        ResponseEntity<Object> response = globalErrorHandler.accountVerificationFailureException(exception, webRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Test message", ((ErrorResponse) Objects.requireNonNull(response.getBody())).getMessage());
    }

    @Test
    public void InsufficientBalanceExceptionTest() {
        WebRequest webRequest = mock(WebRequest.class);
        InsufficientBalanceException exception = new InsufficientBalanceException("Test message");

        ResponseEntity<Object> response = globalErrorHandler.handleInsufficientBalanceException(exception, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Test message", ((ErrorResponse) Objects.requireNonNull(response.getBody())).getMessage());
    }
}
