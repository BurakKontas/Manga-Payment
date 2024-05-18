package com.aburakkontas.manga_payment.application.handlers;

import com.aburakkontas.manga.common.payment.queries.CreatePaymentQuery;
import com.aburakkontas.manga.common.payment.queries.results.CreatePaymentQueryResult;
import com.aburakkontas.manga_payment.domain.dtos.InitiliazeCheckoutFormDTO;
import com.aburakkontas.manga_payment.domain.entities.Item;
import com.aburakkontas.manga_payment.domain.repositories.ItemRepository;
import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_payment.domain.repositories.IyzicoRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZonedDateTime;

@Component
public class CreatePaymentQueryHandler {

    private final IyzicoRepository iyzicoRepository;
    private final ItemRepository itemRepository;

    public CreatePaymentQueryHandler(IyzicoRepository iyzicoRepository, ItemRepository itemRepository) {
        this.iyzicoRepository = iyzicoRepository;
        this.itemRepository = itemRepository;
    }

    @QueryHandler
    public CreatePaymentQueryResult handle(CreatePaymentQuery createPaymentQuery) {

        var items = itemRepository.findByIds(createPaymentQuery.getItemIds());

        if(items.isEmpty()) {
            throw new ExceptionWithErrorCode("No items found", 404);
        }

        if(items.size() != createPaymentQuery.getItemIds().size()) {
            var notFoundItems = createPaymentQuery.getItemIds();
            notFoundItems.removeAll(items.stream().map(Item::getId).toList());
            var notFoundItemsString = notFoundItems.stream().map(Object::toString).reduce("", (a, b) -> a + ", " + b);
            throw new ExceptionWithErrorCode("Some items not found: " + notFoundItemsString , 404);
        }

        var createPaymentDto = new InitiliazeCheckoutFormDTO();
        createPaymentDto.setUserId(createPaymentQuery.getUserId());
        createPaymentDto.setItems(items);
        createPaymentDto.setCallbackUrl(createPaymentQuery.getCallbackUrl());
        createPaymentDto.setEmail(createPaymentQuery.getEmail());
        createPaymentDto.setFirstName(createPaymentQuery.getFirstName());
        createPaymentDto.setLastName(createPaymentQuery.getLastName());

        var checkout = iyzicoRepository.initializeCheck(createPaymentDto);

        var isCompleted = checkout.getStatus().equals("success");

        if(!isCompleted) {
            throw new ExceptionWithErrorCode(checkout.getErrorMessage(), 500);
        }

        var queryResult = new CreatePaymentQueryResult();
        queryResult.setCreated(true);
        queryResult.setCreatedAt(Instant.ofEpochSecond(checkout.getSystemTime()).atZone(ZonedDateTime.now().getZone()));
        queryResult.setUserId(createPaymentQuery.getUserId());
        queryResult.setToken(checkout.getToken());
        queryResult.setCheckoutFormContent(checkout.getCheckoutFormContent());
        queryResult.setTokenExpireTime(String.valueOf(checkout.getTokenExpireTime()));
        queryResult.setPaymentPageUrl(checkout.getPaymentPageUrl());

        return queryResult;

    }
}
