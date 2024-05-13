package com.aburakkontas.manga_payment.api.controllers;

import com.aburakkontas.manga_payment.application.Test;
import com.iyzipay.model.BkmInitialize;
import com.iyzipay.model.CheckoutFormInitialize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

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

//    @PostMapping("/fusionauth-user-verified-callback")
//    @PostMapping("/fusionauth-user-deleted-callback")
}
