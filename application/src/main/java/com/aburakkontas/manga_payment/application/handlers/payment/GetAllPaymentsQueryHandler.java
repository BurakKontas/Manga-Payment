package com.aburakkontas.manga_payment.application.handlers.payment;

import com.aburakkontas.manga.common.payment.queries.GetPaymentsQuery;
import com.aburakkontas.manga.common.payment.queries.results.GetPaymentQueryResult;
import com.aburakkontas.manga.common.payment.queries.results.GetPaymentsQueryResult;
import com.aburakkontas.manga_payment.application.utils.PaymentMapper;
import com.aburakkontas.manga_payment.domain.entities.Item;
import com.aburakkontas.manga_payment.domain.entities.Payment;
import com.aburakkontas.manga_payment.domain.repositories.PaymentRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

@Component
public class GetAllPaymentsQueryHandler {

    private final PaymentRepository paymentRepository;

    public GetAllPaymentsQueryHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @QueryHandler
    public GetPaymentsQueryResult handle(GetPaymentsQuery query) {
        var payments = paymentRepository.findAll();

        var result = new GetPaymentsQueryResult();
        var paymentResults = new ArrayList<GetPaymentQueryResult>();
        for (Payment payment : payments) {
            paymentResults.add(PaymentMapper.mapToQueryResult(payment, null));
        }
        result.setPayments(paymentResults);

        return result;
    }
}
