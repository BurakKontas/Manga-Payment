package com.aburakkontas.manga_payment.application.aggregates;

import com.aburakkontas.manga.common.payment.commands.PaymentReceivedCommand;
import com.aburakkontas.manga.common.payment.events.AddCreditEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

@Aggregate
public class PaymentAggregate {

    @AggregateIdentifier
    private String token;
    private String userId;

    protected PaymentAggregate() {
    }

    @CommandHandler
    public PaymentAggregate(PaymentReceivedCommand command) {
        // handle the command logic here
        if(command.getStatus().equals("SUCCESS")) {
            var addCreditEvent = new AddCreditEvent();
            addCreditEvent.setToken(command.getToken());
            addCreditEvent.setUserId(command.getUserId());

            AggregateLifecycle.apply(addCreditEvent);
        }
    }


    @EventSourcingHandler
    public void on(AddCreditEvent event) {
        this.token = event.getToken();
        this.userId = event.getUserId();
    }

}
