package com.aburakkontas.manga_payment.contracts.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class GetUserCreditResponse {
    private Double credit;
    private ArrayList<?> successfulTransactions;
    private ArrayList<?> failedTransactions;
}
