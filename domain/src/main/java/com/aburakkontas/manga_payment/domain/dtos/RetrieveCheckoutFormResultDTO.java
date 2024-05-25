package com.aburakkontas.manga_payment.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RetrieveCheckoutFormResultDTO {
    private boolean success;
    private Double price;
    private Double paidPrice;
    private String paymentId;
    private String conversationId;
    private String token;
    private String cardType;
    private String cardAssociation;
    private String cardFamily;
    private String lastFourDigits;
    private ArrayList<UUID> itemIds;
}
