package com.project.repository.entity;

import com.project.enums.PaymentResult;
import com.project.enums.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    private String id;
    private String sourceAccountId;
    private String targetAccountId;
    private double amount;
    private String currency;
    private LocalDateTime initiationDate;
    private LocalDateTime completionDate;
    private String reference;
    private PaymentType type;
    private PaymentResult result;

//    @Override
//    public String toString() {
//        return "Transaction{" +
//            "id='" + id + '\'' +
//            ", sourceAccountId='" + sourceAccountId + '\'' +
//            ", targetAccountId='" + targetAccountId + '\'' +
//            ", amount=" + amount +
//            ", currency='" + currency + '\'' +
//            ", initiationDate=" + initiationDate +
//            ", completionDate=" + completionDate +
//            ", reference='" + reference + '\'' +
//            ", type=" + type +
//            ", result=" + result +
//            '}';
//    }

}
