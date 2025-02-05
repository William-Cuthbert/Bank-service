package com.project.controller;

import static org.mockito.Mockito.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyDouble;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dto.transaction.TransactionDtoRequest;
import com.project.dto.transaction.TransactionDtoResponse;
import com.project.enums.PaymentType;
import com.project.repository.TransactionRepository;
import com.project.repository.entity.Account;
import com.project.repository.entity.Transaction;
import com.project.service.AccountService;
import com.project.service.impl.AccountServiceImpl;
import com.project.service.impl.TransactionServiceImpl;
import com.project.service.mapper.TransactionMapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private TransactionServiceImpl transactionService;
  @MockBean
  private TransactionRepository transactionRepository;
  @MockBean
  private TransactionMapper transactionMapper;
  @MockBean
  private AccountService accountService;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void transferApi_Success() throws Exception {
    TransactionDtoRequest request = new TransactionDtoRequest();
    request.setType(PaymentType.TRANSFER);
    request.setSourceAccountId("sourceId");
    request.setTargetAccountId("targetId");
    request.setCurrency("USD");
    request.setAmount(100.00);
    request.setReference("txn01");

    Transaction transaction = new Transaction();
    transaction.setId("txnId");
    transaction.setSourceAccountId("sourceId");
    transaction.setTargetAccountId("targetId");
    transaction.setAmount(100.00);
    transaction.setReference("txn01");

    TransactionDtoResponse dtoResponse = new TransactionDtoResponse();
    dtoResponse.setId("txnId");
    dtoResponse.setSourceAccountId("sourceId");
    dtoResponse.setTargetAccountId("targetId");
    dtoResponse.setAmount(100.00);
    dtoResponse.setReference("txn01");

    when(transactionMapper.toEntity(any(TransactionDtoRequest.class))).thenReturn(transaction);
    when(transactionMapper.toDto(any(Transaction.class))).thenReturn(dtoResponse);
    when(transactionService.createTransaction(eq(PaymentType.TRANSFER), anyString(), anyString(), anyDouble(), anyString())).thenReturn(transaction);

    mockMvc.perform(post("/v1/api/payment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());
  }
}
