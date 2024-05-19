package com.aburakkontas.manga_payment.contracts.response;

import lombok.Data;

import java.util.UUID;

@Data
public class DeleteItemResponse {
    private boolean deleted;
    private UUID itemId;
}
