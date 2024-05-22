package com.aburakkontas.manga_payment.application.handlers.payment;

import com.aburakkontas.manga.common.auth.queries.GetUserDetailsByIdQuery;
import com.aburakkontas.manga.common.auth.queries.results.GetUserDetailsByIdQueryResult;
import com.aburakkontas.manga.common.payment.queries.GetPaymentQuery;
import com.aburakkontas.manga.common.payment.queries.results.GetPaymentQueryResult;
import com.aburakkontas.manga_payment.application.utils.PaymentMapper;
import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_payment.domain.repositories.PaymentRepository;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class GetPaymentQueryHandler {

    private final PaymentRepository paymentRepository;
    private final QueryGateway queryGateway;

    public GetPaymentQueryHandler(PaymentRepository paymentRepository, QueryGateway queryGateway) {
        this.paymentRepository = paymentRepository;
        this.queryGateway = queryGateway;
    }

    @QueryHandler
    public GetPaymentQueryResult handle(GetPaymentQuery query) {
        if(query.getExecutorId() != query.getUserId()) {
            var userQuery = GetUserDetailsByIdQuery.builder()
                    .userId(query.getUserId())
                    .build();

            var result = queryGateway.query(userQuery, GetUserDetailsByIdQueryResult.class).join();

            if(!result.getPermissions().contains("Admin")) {
                throw new ExceptionWithErrorCode("You are not authorized to see this payment", 403);
            }
        }

        var payment = paymentRepository.findById(query.getPaymentId()).orElseThrow(
            () -> new ExceptionWithErrorCode("Payment not found with ID: " + query.getPaymentId(), 404)
        );
        return PaymentMapper.mapToQueryResult(payment, query.getExecutorId());
    }
}
