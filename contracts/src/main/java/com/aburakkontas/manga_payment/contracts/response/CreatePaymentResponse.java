package com.aburakkontas.manga_payment.contracts.response;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class CreatePaymentResponse {
    private boolean created;
    private ZonedDateTime createdAt;
    private UUID userId; // iyzico conversationId
    private String token;
    private String checkoutFormContent;
    private String tokenExpireTime;
    private String paymentPageUrl;
}
