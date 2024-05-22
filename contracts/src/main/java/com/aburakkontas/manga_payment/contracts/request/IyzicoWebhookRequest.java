package com.aburakkontas.manga_payment.contracts.request;

import lombok.Data;

import java.util.UUID;

@Data
public class IyzicoWebhookRequest {
    private String paymentConversationId;
    private Integer merchantId;
    private UUID token;
    private String status;
    private UUID iyziReferenceCode;
    private String iyziEventType;
    private Long iyziEventTime;
    private Integer iyziPaymentId;
}
