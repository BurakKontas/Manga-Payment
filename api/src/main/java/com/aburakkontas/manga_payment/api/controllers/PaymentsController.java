package com.aburakkontas.manga_payment.api.controllers;

import com.aburakkontas.manga.common.payment.queries.CreatePaymentQuery;
import com.aburakkontas.manga.common.payment.queries.results.CreatePaymentQueryResult;
import com.aburakkontas.manga_payment.contracts.request.CreatePaymentRequest;
import com.aburakkontas.manga_payment.contracts.response.CreatePaymentResponse;
import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_payment.domain.primitives.Result;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("/api/v1/payments")
public class PaymentsController {

    private final QueryGateway  queryGateway;

    @Autowired
    public PaymentsController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @PostMapping("/create")
    public ResponseEntity<Result<CreatePaymentResponse>> createPayment(@RequestBody CreatePaymentRequest request, Authentication authentication) {
        var email = authentication.getPrincipal();

        var query = CreatePaymentQuery.builder()
                .userId(UUID.fromString(authentication.getCredentials().toString()))
                .firstName(email.toString())
                .lastName(email.toString())
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

    @GetMapping("/get")
    public void getPayment(@RequestParam UUID paymentId, Authentication authentication) {
        var isUserAdmin = isUserAdmin(authentication);
        //get payment admins can access all payments
    }

    @GetMapping("/get-all")
    public void getPayments() {
        // only admin can access this endpoint
        //  get payments
    }

    @GetMapping("/get-by-user")
    public void getPaymentsByUser(@RequestParam UUID userId, Authentication authentication) {
        var isUserAdmin = isUserAdmin(authentication);

        // admins can access all users payments
        //  get payments by user
    }

    private boolean isUserAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("Admin"));
    }


}
