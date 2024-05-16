package com.aburakkontas.manga_payment.api.controllers;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private final QueryGateway  queryGateway;

    @Autowired
    public PaymentController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

}
