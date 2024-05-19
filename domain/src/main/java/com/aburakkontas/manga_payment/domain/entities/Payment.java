package com.aburakkontas.manga_payment.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
public class Payment {
    @Id
    private String paymentId;
    private UUID userId;
    private Double price;
    private ZonedDateTime paymentDate;
    private String cardType;
    private String cardLastFourDigits;
    private String cardFamily;
    private String cardAssociation;

    @ManyToMany(cascade = CascadeType.ALL)
    private ArrayList<Item> items;
}