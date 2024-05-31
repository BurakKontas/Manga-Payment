package com.aburakkontas.manga_payment.domain.entities.payment;

import com.aburakkontas.manga_payment.domain.entities.item.Item;
import com.aburakkontas.manga_payment.domain.entities.usercredit.UserCredit;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payments")
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
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
    private List<Item> items = List.of();

    protected Payment() {

    }
}
