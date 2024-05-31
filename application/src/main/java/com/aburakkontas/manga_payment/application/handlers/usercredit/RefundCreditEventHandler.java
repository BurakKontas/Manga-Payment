package com.aburakkontas.manga_payment.application.handlers.usercredit;

import com.aburakkontas.manga.common.payment.events.DeductCreditEvent;
import com.aburakkontas.manga.common.payment.events.RefundCreditEvent;
import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_payment.domain.repositories.UserCreditRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class RefundCreditEventHandler {

    private final UserCreditRepository userCreditRepository;

    public RefundCreditEventHandler(UserCreditRepository userCreditRepository) {
        this.userCreditRepository = userCreditRepository;
    }

    @EventHandler
    public void handle(RefundCreditEvent userCreditCreatedEvent) {
        var userId = userCreditCreatedEvent.getUserId();

        var user = userCreditRepository.findById(userId).orElseThrow(() -> new ExceptionWithErrorCode("User not found", 404));

        var isSuccess = user.addCredit(userCreditCreatedEvent.getCredit());

        if(!isSuccess) {
            throw new ExceptionWithErrorCode("Failed to refund credit", 500);
        }

        userCreditRepository.save(user);
    }
}