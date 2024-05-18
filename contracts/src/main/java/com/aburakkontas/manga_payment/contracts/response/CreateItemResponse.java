package com.aburakkontas.manga_payment.contracts.response;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateItemResponse {
    private boolean created;
    private UUID itemId;
}
