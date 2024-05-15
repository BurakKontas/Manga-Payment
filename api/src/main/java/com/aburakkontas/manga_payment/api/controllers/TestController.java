package com.aburakkontas.manga_payment.api.controllers;

import com.aburakkontas.manga.common.auth.queries.*;
import com.aburakkontas.manga.common.auth.queries.results.*;
import com.aburakkontas.manga_payment.application.Test;
import com.aburakkontas.manga_payment.domain.primitives.Result;
import com.iyzipay.model.BkmInitialize;
import com.iyzipay.model.CheckoutFormInitialize;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final QueryGateway  queryGateway;

    @Autowired
    public TestController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }
    @GetMapping("/test")
    public String test() {
        Test.TestMe();
        return "Test";
    }

    @GetMapping("/test2")
    public CheckoutFormInitialize test2() {
        var result = Test.TestMe2();
        return result;
    }

    @GetMapping("/test3")
    public BkmInitialize test3() {
        var result = Test.TestMe3();
        return result;
    }

    @PostMapping("/payment-callback")
    public String paymentCallback(@RequestBody Object data) {
        System.out.println(data);
        return "OK";
    }

    @GetMapping("/test4")
    public ResponseEntity<Result<GetAllErrorCodesQueryResult>> test4() {
        var query = GetAllErrorCodesQuery.builder()
                .build();

        var result = queryGateway.query(query, GetAllErrorCodesQueryResult.class).join();

        return ResponseEntity.ok(Result.success(result));
    }

//    @PostMapping("/fusionauth-user-verified-callback")
//    @PostMapping("/fusionauth-user-deleted-callback")
}
