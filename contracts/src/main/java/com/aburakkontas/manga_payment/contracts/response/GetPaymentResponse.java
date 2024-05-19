package com.aburakkontas.manga_payment.contracts.response;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Data
public class GetPaymentResponse {
    private UUID userId;
    private String paymentId;
    private Double price;
    private ZonedDateTime paymentDate;
    private String cardType;
    private String cardLastFourDigits;
    private String cardFamily;
    private String cardAssociation;
    private ArrayList<UUID> itemIds;
}
