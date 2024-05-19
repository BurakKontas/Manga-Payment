package com.aburakkontas.manga_payment.contracts.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class GetPaymentsByUserResponse {
    private List<GetPaymentResponse> payments;
    private UUID userId;
    private UUID executorId;
}
