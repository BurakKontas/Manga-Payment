package com.aburakkontas.manga_payment.application.sagas;

import com.aburakkontas.manga.common.payment.commands.AddCreditCommand;
import com.aburakkontas.manga.common.payment.commands.PaymentReceivedCommand;
import com.aburakkontas.manga.common.payment.events.AddCreditEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
public class PaymentReceivedSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "paymentId")
    public void handle(PaymentReceivedCommand event) {
        if(!event.getStatus().equals("success")) SagaLifecycle.end();

        SagaLifecycle.associateWith("paymentId", event.getPaymentId());
        SagaLifecycle.associateWith("userId", event.getUserId());
        SagaLifecycle.associateWith("token", event.getToken());

        var addCreditCommand = AddCreditCommand.builder()
                .paymentId(event.getPaymentId())
                .userId(event.getUserId())
                .token(event.getToken())
                .build();

        commandGateway.send(addCreditCommand);
    }

    @SagaEventHandler(associationProperty = "token")
    public void handle(AddCreditEvent event) {
        SagaLifecycle.end();
    }

}
