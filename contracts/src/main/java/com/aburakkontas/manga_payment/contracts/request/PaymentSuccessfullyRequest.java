package com.aburakkontas.manga_payment.contracts.request;

import lombok.Data;

@Data
public class PaymentSuccessfullyRequest {
    private String token;
}
