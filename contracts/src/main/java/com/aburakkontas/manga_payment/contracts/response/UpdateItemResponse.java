package com.aburakkontas.manga_payment.contracts.response;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateItemResponse {
    private boolean updated;
    private UUID itemId;
    private String imageUri;
}
