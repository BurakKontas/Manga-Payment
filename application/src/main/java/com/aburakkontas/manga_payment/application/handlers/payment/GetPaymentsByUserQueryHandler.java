package com.aburakkontas.manga_payment.application.handlers.payment;

import com.aburakkontas.manga.common.payment.queries.GetPaymentsByUserQuery;
import com.aburakkontas.manga.common.payment.queries.results.GetPaymentQueryResult;
import com.aburakkontas.manga.common.payment.queries.results.GetPaymentsByUserQueryResult;
import com.aburakkontas.manga_payment.application.utils.PaymentMapper;
import com.aburakkontas.manga_payment.domain.repositories.PaymentRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class GetPaymentsByUserQueryHandler {

    private final PaymentRepository paymentRepository;

    public GetPaymentsByUserQueryHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @QueryHandler
    public GetPaymentsByUserQueryResult handle(GetPaymentsByUserQuery query) {
        var page = PageRequest.of(query.getPage(), query.getSize());
        var payments = paymentRepository.findPaymentsWithFilters(query.getFrom(), query.getTo(), query.getUserId(), query.getSort(), query.getOrder(), page);

        var paymentList = new ArrayList<GetPaymentQueryResult>();

        payments.forEach(payment -> {
            var paymentResult = PaymentMapper.mapToQueryResult(payment);
            paymentList.add(paymentResult);
        });

        var queryResult = new GetPaymentsByUserQueryResult();
        queryResult.setPayments(paymentList);

        return queryResult;
    }
}
