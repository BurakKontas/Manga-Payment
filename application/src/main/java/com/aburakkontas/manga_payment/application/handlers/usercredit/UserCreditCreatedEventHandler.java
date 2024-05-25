package com.aburakkontas.manga_payment.application.handlers.usercredit;

import com.aburakkontas.manga.common.payment.events.UserCreditCreatedEvent;
import com.aburakkontas.manga_payment.domain.entities.usercredit.UserCredit;
import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_payment.domain.repositories.UserCreditRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserCreditCreatedEventHandler {

    private final UserCreditRepository userCreditRepository;

    public UserCreditCreatedEventHandler(UserCreditRepository userCreditRepository) {
        this.userCreditRepository = userCreditRepository;
    }

    @EventHandler
    public void handle(UserCreditCreatedEvent userCreditCreatedEvent) {
        var userId = userCreditCreatedEvent.getUserId();

        if(userCreditRepository.existsById(UUID.fromString(userId))) {
            throw new ExceptionWithErrorCode("User already exists", 400);
        }

        var user = UserCredit.create(UUID.fromString(userId));

        userCreditRepository.save(user);
    }
}
