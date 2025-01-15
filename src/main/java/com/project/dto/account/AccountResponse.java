package com.project.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.project.repository.entity.Transaction;

import javax.persistence.OneToMany;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private String id;
    private String fullName;
    private String bankName;
    private String phoneNumber;
    private String emailAddress;
    private String accountNumber;
    private String sortCode;
    private double balance;
    @OneToMany private List<Transaction> transactions;
}
