package com.aburakkontas.manga_payment.application.handlers.usercredit;

import com.aburakkontas.manga.common.payment.events.DeductCreditEvent;
import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_payment.domain.repositories.UserCreditRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class DeductCreditEventHandler {

    private final UserCreditRepository userCreditRepository;

    public DeductCreditEventHandler(UserCreditRepository userCreditRepository) {
        this.userCreditRepository = userCreditRepository;
    }

    @EventHandler
    public void handle(DeductCreditEvent userCreditCreatedEvent) {
        var userId = userCreditCreatedEvent.getUserId();

        var user = userCreditRepository.findById(userId).orElseThrow(() -> new ExceptionWithErrorCode("User not found", 404));

        var isSuccess = user.subtractCredit(userCreditCreatedEvent.getCredit());

        if(!isSuccess) {
            throw new ExceptionWithErrorCode("Not enough credit", 400);
        }

        userCreditRepository.save(user);
    }
}