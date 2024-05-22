package com.aburakkontas.manga_payment.domain.dtos;

import com.aburakkontas.manga_payment.domain.entities.item.Item;
import lombok.Data;

import java.util.ArrayList;
import java.util.UUID;

@Data
public class InitiliazeCheckoutFormDTO {
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private ArrayList<Item> items;
    private String callbackUrl;
    private String currency = "TRY";
}
