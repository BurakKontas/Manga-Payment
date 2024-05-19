package com.aburakkontas.manga_payment.api.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping(path = "/api/v1/webhook")
public class WebhookController {

    //https://docs.iyzico.com/ek-servisler/webhook#direkt-format-1
    //iyzicodan gelen webhooklerin alındığı endpoint
    //iyzico tarafından belirtilen secret key ile gelen header kontrol edilir
    //eğer secret key doğru ise işlem yapılır
    //eğer secret key yanlış ise işlem yapılmaz
    //eğer key doğru ise commandGateway ile işlem yapılır.
    //command işlenme sırasında yine commandGateway ile kullanıcının coin i arttırılır.
}