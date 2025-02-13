package com.project.repository.entity;

import com.project.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    private String id;
    private String fullName;
    private String bankName;
    private String phoneNumber;
    private String emailAddress;
    private String accountNumber;
    private String sortCode;
    private double balance;
    private Status status;
    @OneToMany
    private List<Transaction> transactions;
}