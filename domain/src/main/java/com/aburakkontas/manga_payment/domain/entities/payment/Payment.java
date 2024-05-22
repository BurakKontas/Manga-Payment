package com.aburakkontas.manga_payment.domain.entities.payment;

import com.aburakkontas.manga_payment.domain.entities.item.Item;
import com.aburakkontas.manga_payment.domain.entities.usercredit.UserCredit;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.ArrayList;

@Entity
@Table(name = "payments")
@Data
public class Payment {
    @Id
    private String paymentId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserCredit user;
    private Double price;
    private ZonedDateTime paymentDate;
    private String cardType;
    private String cardLastFourDigits;
    private String cardFamily;
    private String cardAssociation;

    @ManyToMany(cascade = CascadeType.ALL)
    private ArrayList<Item> items;
}
