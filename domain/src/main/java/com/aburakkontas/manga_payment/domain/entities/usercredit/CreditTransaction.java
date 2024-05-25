package com.aburakkontas.manga_payment.domain.entities.usercredit;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
public class CreditTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;

    private UUID userId;
    private ZonedDateTime transactionDate;
    private CreditTransactionTypes transactionType;
    private Double amount;
    private Double balance;
    private String description;
    private boolean transactionSuccess;
    private String paymentId;
}
