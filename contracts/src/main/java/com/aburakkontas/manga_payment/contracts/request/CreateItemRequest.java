package com.aburakkontas.manga_payment.contracts.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateItemRequest {
    private String name;
    private String description;
    private Double price;
    private String category;
    private UUID imageId;
    private String itemType;

}
