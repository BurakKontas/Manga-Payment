package com.aburakkontas.manga_payment.application.handlers.usercredit;

import com.aburakkontas.manga.common.payment.events.AddCreditEvent;
import com.aburakkontas.manga_payment.domain.dtos.RetrieveCheckoutFormDTO;
import com.aburakkontas.manga_payment.domain.entities.payment.Payment;
import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_payment.domain.repositories.ItemRepository;
import com.aburakkontas.manga_payment.domain.repositories.IyzicoRepository;
import com.aburakkontas.manga_payment.domain.repositories.PaymentRepository;
import com.aburakkontas.manga_payment.domain.repositories.UserCreditRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.UUID;

@Component
public class AddCreditEventHandler {

    private final IyzicoRepository iyzicoRepository;
    private final PaymentRepository paymentRepository;
    private final ItemRepository itemRepository;
    private final UserCreditRepository userCreditRepository;

    public AddCreditEventHandler(
        IyzicoRepository iyzicoRepository,
        PaymentRepository paymentRepository,
        ItemRepository itemRepository,
        UserCreditRepository userCreditRepository
    ) {
        this.iyzicoRepository = iyzicoRepository;
        this.paymentRepository = paymentRepository;
        this.itemRepository = itemRepository;
        this.userCreditRepository = userCreditRepository;
    }

    @EventHandler
    public void handle(AddCreditEvent addCreditEvent) {
        var userId = addCreditEvent.getUserId();
        var token = addCreditEvent.getToken();

        var retrieveCheckoutFormDto = new RetrieveCheckoutFormDTO(userId, token);

        var checkoutForm = iyzicoRepository.retrieveCheckoutForm(retrieveCheckoutFormDto);

        var user = userCreditRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ExceptionWithErrorCode("User not found", 404));

        user.addCredit(checkoutForm.getPrice());

        var items = itemRepository.findByIds(checkoutForm.getItemIds());

        var payment = Payment.builder()
            .paymentId(checkoutForm.getPaymentId())
            .user(user)
            .price(checkoutForm.getPrice())
            .paymentDate(ZonedDateTime.now())
            .cardType(checkoutForm.getCardType())
            .cardAssociation(checkoutForm.getCardAssociation())
            .cardFamily(checkoutForm.getCardFamily())
            .cardLastFourDigits(checkoutForm.getLastFourDigits())
            .items(items)
            .build();

        paymentRepository.save(payment);
    }
}
