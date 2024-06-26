package com.aburakkontas.manga_payment.application.utils;

import com.aburakkontas.manga.common.payment.queries.results.GetPaymentQueryResult;
import com.aburakkontas.manga_payment.domain.entities.item.Item;
import com.aburakkontas.manga_payment.domain.entities.payment.Payment;

import java.util.ArrayList;
import java.util.UUID;

public class PaymentMapper {
    public static GetPaymentQueryResult mapToQueryResult(Payment payment) {
        var queryResult = new GetPaymentQueryResult();
        queryResult.setPaymentId(payment.getPaymentId());
        queryResult.setUserId(payment.getUser().getUserId());
        queryResult.setCardAssociation(payment.getCardAssociation());
        queryResult.setCardFamily(payment.getCardFamily());
        queryResult.setCardType(payment.getCardType());
        queryResult.setCardLastFourDigits(payment.getCardLastFourDigits());
        queryResult.setPrice(payment.getPrice());
        queryResult.setPaymentDate(payment.getPaymentDate());

        var itemIds = new ArrayList<UUID>();
        for (Item item : payment.getItems()) {
            itemIds.add(item.getId());
        }

        queryResult.setItemIds(itemIds);

        return queryResult;
    }
}
