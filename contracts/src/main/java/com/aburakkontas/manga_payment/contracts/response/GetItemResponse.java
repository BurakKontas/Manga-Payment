package com.aburakkontas.manga_payment.contracts.response;

import lombok.Data;

import java.util.UUID;

@Data
public class GetItemResponse {
    private UUID itemId;
    private String name;
    private String description;
    private Double price;
    private String category;
    private UUID imageId;
    private String itemType;
    private String imageUri;
}
