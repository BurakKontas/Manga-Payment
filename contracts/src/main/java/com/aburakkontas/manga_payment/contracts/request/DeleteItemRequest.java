package com.aburakkontas.manga_payment.contracts.request;

import lombok.Data;

import java.util.UUID;

@Data
public class DeleteItemRequest {
    private UUID itemId;
}
