package com.aburakkontas.manga_payment.contracts.response;

import lombok.Data;

import java.util.List;

@Data
public class GetPaymentsResponse {
    private List<GetPaymentResponse> payments;
}
