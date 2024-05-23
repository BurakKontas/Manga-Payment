package com.aburakkontas.manga_payment.contracts.request;

import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;

@Data
public class CreatePaymentRequest {
    private ArrayList<UUID> itemIds;
    private String currency;
}
