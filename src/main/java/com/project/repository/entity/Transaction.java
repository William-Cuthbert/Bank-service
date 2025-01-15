package com.project.repository.entity;

import com.project.enums.PaymentResult;
import com.project.enums.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id private String id;
    private String sourceAccountId;
    private String targetAccountId;
    private String fullName;
    private double amount;
    private String currency;
    private String initiationDate;
    private String completionDate;
    private String reference;
    private PaymentType type;
    private PaymentResult result;
}
