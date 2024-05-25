package com.aburakkontas.manga_payment.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RetrieveCheckoutFormDTO {
    private String conversationId;
    private String token;
}
