package com.aburakkontas.manga_payment.api.controllers;

import com.aburakkontas.manga.common.payment.commands.CreateUserCreditCommand;
import com.aburakkontas.manga.common.payment.commands.PaymentReceivedCommand;
import com.aburakkontas.manga_payment.contracts.request.FusionAuthEmailVerifiedWebhookRequest;
import com.aburakkontas.manga_payment.contracts.request.IyzicoWebhookRequest;
import io.swagger.v3.oas.annotations.Hidden;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.text.MessageFormat;

@Hidden
@RestController
@RequestMapping(path = "/api/v1/webhook")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);
    private final CommandGateway commandGateway;

    @Autowired
    public WebhookController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/fusion-verified")
    public ResponseEntity<Void> fusionVerified(@RequestBody FusionAuthEmailVerifiedWebhookRequest request) {
        var createUserCreditCommand = CreateUserCreditCommand.builder()
                .userId(request.getEvent().getUser().getId())
                .build();

        commandGateway.sendAndWait(createUserCreditCommand);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/iyzico")
    public ResponseEntity<Void> iyzico(@RequestBody IyzicoWebhookRequest request) {
        var paymentReceivedCommand = new PaymentReceivedCommand();
        paymentReceivedCommand.setPaymentId(String.valueOf(request.getIyziPaymentId()));
        paymentReceivedCommand.setStatus(request.getStatus());
        paymentReceivedCommand.setToken(String.valueOf(request.getToken()));
        paymentReceivedCommand.setUserId(request.getPaymentConversationId());

        commandGateway.sendAndWait(paymentReceivedCommand);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/payment-successfully")
    public ResponseEntity<Void> paymentSuccessfully(@RequestParam String callbackUrl, @RequestBody String request) {
        var token = request.split("=")[1];
        log.info("payment successfully webhook received: {}", token);
        var url = MessageFormat.format("{0}#paymentToken={1}", callbackUrl, token);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }
}