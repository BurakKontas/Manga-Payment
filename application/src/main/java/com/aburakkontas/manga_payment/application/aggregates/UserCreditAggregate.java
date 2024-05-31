package com.aburakkontas.manga_payment.application.aggregates;

import com.aburakkontas.manga.common.payment.commands.AddCreditCommand;
import com.aburakkontas.manga.common.payment.commands.CreateUserCreditCommand;
import com.aburakkontas.manga.common.payment.commands.DeductCreditCommand;
import com.aburakkontas.manga.common.payment.commands.RefundCreditCommand;
import com.aburakkontas.manga.common.payment.events.AddCreditEvent;
import com.aburakkontas.manga.common.payment.events.DeductCreditEvent;
import com.aburakkontas.manga.common.payment.events.RefundCreditEvent;
import com.aburakkontas.manga.common.payment.events.UserCreditCreatedEvent;
import com.aburakkontas.manga_payment.domain.repositories.UserCreditRepository;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Aggregate
public class UserCreditAggregate {

    @AggregateIdentifier
    private UUID id = UUID.randomUUID();
    private String userId;

    protected UserCreditAggregate() {
    }

    @CommandHandler
    public UserCreditAggregate(CreateUserCreditCommand createUserCreditCommand) {
        var userCreditCreatedEvent = new UserCreditCreatedEvent();
        userCreditCreatedEvent.setUserId(createUserCreditCommand.getUserId());
        userCreditCreatedEvent.setCredit(createUserCreditCommand.getCredit());
        userCreditCreatedEvent.setPayments(createUserCreditCommand.getPayments());

        AggregateLifecycle.apply(userCreditCreatedEvent);
    }

    @EventSourcingHandler
    public void on(UserCreditCreatedEvent event) {
        this.userId = event.getUserId();
    }

    @CommandHandler
    public UserCreditAggregate(AddCreditCommand command) {
        var addCreditEvent = new AddCreditEvent();
        addCreditEvent.setUserId(command.getUserId());
        addCreditEvent.setToken(command.getToken());

        AggregateLifecycle.apply(addCreditEvent);
    }

    @EventSourcingHandler
    public void on(AddCreditEvent event) {
        this.userId = event.getUserId();
    }

    @CommandHandler
    public UserCreditAggregate(DeductCreditCommand command) {
        var deductCreditEvent = new DeductCreditEvent();
        deductCreditEvent.setUserId(command.getUserId());
        deductCreditEvent.setCredit(command.getCredit());

        AggregateLifecycle.apply(deductCreditEvent);
    }

    @EventSourcingHandler
    public void on(DeductCreditEvent event) {
        this.userId = String.valueOf(event.getUserId());
    }

    @CommandHandler
    public UserCreditAggregate(RefundCreditCommand command) {
        var userCreditRefundEvent = new RefundCreditEvent();
        userCreditRefundEvent.setUserId(command.getUserId());
        userCreditRefundEvent.setCredit(command.getCredit());

        AggregateLifecycle.apply(userCreditRefundEvent);
    }

    @EventSourcingHandler
    public void on(RefundCreditEvent event) {
        this.userId = String.valueOf(event.getUserId());
    }
}
