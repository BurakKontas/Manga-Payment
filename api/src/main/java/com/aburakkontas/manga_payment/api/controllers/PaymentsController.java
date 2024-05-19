package com.aburakkontas.manga_payment.api.controllers;

import com.aburakkontas.manga.common.payment.queries.CreatePaymentQuery;
import com.aburakkontas.manga.common.payment.queries.GetPaymentQuery;
import com.aburakkontas.manga.common.payment.queries.GetPaymentsByUserQuery;
import com.aburakkontas.manga.common.payment.queries.GetPaymentsQuery;
import com.aburakkontas.manga.common.payment.queries.results.CreatePaymentQueryResult;
import com.aburakkontas.manga.common.payment.queries.results.GetPaymentQueryResult;
import com.aburakkontas.manga.common.payment.queries.results.GetPaymentsByUserQueryResult;
import com.aburakkontas.manga.common.payment.queries.results.GetPaymentsQueryResult;
import com.aburakkontas.manga_payment.contracts.request.CreatePaymentRequest;
import com.aburakkontas.manga_payment.contracts.response.CreatePaymentResponse;
import com.aburakkontas.manga_payment.contracts.response.GetPaymentResponse;
import com.aburakkontas.manga_payment.contracts.response.GetPaymentsByUserResponse;
import com.aburakkontas.manga_payment.contracts.response.GetPaymentsResponse;
import com.aburakkontas.manga_payment.domain.primitives.Result;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentsController {

    private final QueryGateway  queryGateway;

    @Autowired
    public PaymentsController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @PostMapping("/create-payment")
    public ResponseEntity<Result<CreatePaymentResponse>> createPayment(@RequestBody CreatePaymentRequest request, Authentication authentication) {
        var email = authentication.getPrincipal();

        var query = CreatePaymentQuery.builder()
                .userId(UUID.fromString(authentication.getCredentials().toString()))
                .firstName(email.toString())
                .lastName(email.toString())
                .email(email.toString())
                .itemIds(request.getItemIds())
                .callbackUrl(request.getCallbackUrl())
                .currency(request.getCurrency())
                .build();

        var result = queryGateway.query(query, CreatePaymentQueryResult.class).join();

        var response = new CreatePaymentResponse();
        response.setCreatedAt(result.getCreatedAt());
        response.setCreated(result.isCreated());
        response.setUserId(result.getUserId());
        response.setToken(result.getToken());
        response.setCheckoutFormContent(result.getCheckoutFormContent());
        response.setTokenExpireTime(result.getTokenExpireTime());
        response.setPaymentPageUrl(result.getPaymentPageUrl());

        return ResponseEntity.ok(Result.success(response));
    }

    @GetMapping("/get-payment")
    public ResponseEntity<Result<GetPaymentResponse>> getPayment(
            @RequestParam String paymentId,
            @RequestParam UUID userId,
            Authentication authentication
    ) {
        var executorId = UUID.fromString(authentication.getCredentials().toString());

        if(userId == null) userId = executorId;

        var query = GetPaymentQuery.builder()
                .userId(userId)
                .paymentId(paymentId)
                .executorId(executorId)
                .build();

        var result = queryGateway.query(query, GetPaymentQueryResult.class).join();

        var response = getPaymentResponse(result);

        return ResponseEntity.ok(Result.success(response));
    }

    @GetMapping("/get-all")
    public ResponseEntity<Result<Void>> getPayments(
        @RequestParam ZonedDateTime from,
        @RequestParam ZonedDateTime to,
        @RequestParam String sort,
        @RequestParam Integer page,
        @RequestParam Integer size,
        @RequestParam String order
    ) {
        var query = GetPaymentsQuery.builder()
                .from(from)
                .to(to)
                .sort(sort)
                .page(page)
                .size(size)
                .order(order)
                .build();

        var result = queryGateway.query(query, GetPaymentsQueryResult.class).join();

        var response = new GetPaymentsResponse();

        // convert result to response
        var payments = new ArrayList<GetPaymentResponse>();
        for (var payment : result.getPayments()) {
            var paymentResponse = getPaymentResponse(payment);
            payments.add(paymentResponse);
        }
        response.setPayments(payments);

        return ResponseEntity.ok(Result.success(null));
    }

    @GetMapping("/get-payments-by-user")
    public ResponseEntity<Result<GetPaymentsByUserResponse>> getPaymentsByUser(
            @RequestParam UUID userId,
            @RequestParam ZonedDateTime from,
            @RequestParam ZonedDateTime to,
            @RequestParam String sort,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String order,
            Authentication authentication
    ) {
        var executorId = UUID.fromString(authentication.getCredentials().toString());
        if(userId == null) userId = executorId;

        var query = GetPaymentsByUserQuery.builder()
                .from(from)
                .to(to)
                .sort(sort)
                .page(page)
                .size(size)
                .order(order)
                .userId(userId)
                .executorId(executorId)
                .build();

        var result = queryGateway.query(query, GetPaymentsByUserQueryResult.class).join();

        var response = new GetPaymentsByUserResponse();

        // convert result to response
        var payments = new ArrayList<GetPaymentResponse>();
        for (var payment : result.getPayments()) {
            var paymentResponse = getPaymentResponse(payment);
            payments.add(paymentResponse);
        }
        response.setPayments(payments);
        response.setUserId(userId);
        response.setExecutorId(executorId);

        return ResponseEntity.ok(Result.success(response));
    }



    private static GetPaymentResponse getPaymentResponse(GetPaymentQueryResult payment) {
        var paymentResponse = new GetPaymentResponse();
        paymentResponse.setUserId(payment.getUserId());
        paymentResponse.setPaymentId(payment.getPaymentId());
        paymentResponse.setPrice(payment.getPrice());
        paymentResponse.setPaymentDate(payment.getPaymentDate());
        paymentResponse.setCardType(payment.getCardType());
        paymentResponse.setCardLastFourDigits(payment.getCardLastFourDigits());
        paymentResponse.setCardFamily(payment.getCardFamily());
        paymentResponse.setCardAssociation(payment.getCardAssociation());
        paymentResponse.setItemIds(payment.getItemIds());
        return paymentResponse;
    }

}
