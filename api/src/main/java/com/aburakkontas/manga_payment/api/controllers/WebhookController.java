package com.aburakkontas.manga_payment.api.controllers;

import com.aburakkontas.manga_payment.contracts.request.FusionAuthEmailVerifiedWebhookRequest;
import com.aburakkontas.manga_payment.contracts.request.IyzicoWebhookRequest;
import io.swagger.v3.oas.annotations.Hidden;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Hidden
@RestController
@RequestMapping(path = "/api/v1/webhook")
public class WebhookController {

    private final CommandGateway commandGateway;

    @Autowired
    public WebhookController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/fusion-verified")
    public ResponseEntity<Void> fusionVerified(@RequestBody FusionAuthEmailVerifiedWebhookRequest request) {

        System.out.println("Fusion");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/iyzico")
    public ResponseEntity<Void> iyzico(@RequestBody IyzicoWebhookRequest request) {
        System.out.println("iyzico");
        return ResponseEntity.ok().build();
    }

    //https://docs.iyzico.com/ek-servisler/webhook#direkt-format-1
    //iyzicodan gelen webhooklerin alındığı endpoint
    //iyzico tarafından belirtilen secret key ile gelen header kontrol edilir
    //eğer secret key doğru ise işlem yapılır
    //eğer secret key yanlış ise işlem yapılmaz
    //eğer key doğru ise commandGateway ile işlem yapılır.
    //command işlenme sırasında yine commandGateway ile kullanıcının coin i arttırılır.
}