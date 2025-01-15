package com.project.errorhandler;

import com.project.dto.ErrorResponse;
import com.project.errorhandler.exception.AccountExistsException;
import com.project.errorhandler.exception.AccountNotFoundException;
import com.project.errorhandler.exception.AccountVerificationFailureException;
import com.project.errorhandler.exception.TransactionNotFoundException;
import com.project.errorhandler.exception.InsufficientBalanceException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(value = {AccountNotFoundException.class})
    public ResponseEntity<Object> accountNotFoundException(AccountNotFoundException ex, WebRequest request) {
        String requestURI = request.getDescription(false);
        log.error("Request URI: {}, Exception: {}", requestURI, ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Account not found", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {TransactionNotFoundException.class})
    public ResponseEntity<Object> transactionNotFoundException(TransactionNotFoundException ex, WebRequest request) {
        String requestURI = request.getDescription(false);
        log.error("Request URI: {}, Exception: {}", requestURI, ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Transaction not found", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {AccountVerificationFailureException.class})
    public ResponseEntity<Object> accountVerificationFailureException(AccountVerificationFailureException ex, WebRequest request) {
        String requestURI = request.getDescription(false);
        log.error("Request URI: {}, Exception: {}", requestURI, ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Failed to verify account", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {AccountExistsException.class})
    public ResponseEntity<Object> accountExistsException(AccountExistsException ex, WebRequest request) {
        String requestURI = request.getDescription(false);
        log.error("Request URI: {}, Exception: {}", requestURI, ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Account already exists", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Object> handleInsufficientBalanceException(InsufficientBalanceException ex, WebRequest request) {
        String requestURI = request.getDescription(false);
        log.error("Request URI: {}, Exception: {}", requestURI, ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Insufficient Balance", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
