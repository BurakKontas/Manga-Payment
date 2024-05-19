package com.aburakkontas.manga_payment.application.handlers;

import com.aburakkontas.manga.common.payment.queries.CreatePaymentQuery;
import com.aburakkontas.manga.common.payment.queries.results.CreatePaymentQueryResult;
import com.aburakkontas.manga_payment.domain.dtos.InitiliazeCheckoutFormDTO;
import com.aburakkontas.manga_payment.domain.entities.Item;
import com.aburakkontas.manga_payment.domain.repositories.ItemRepository;
import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_payment.domain.repositories.IyzicoRepository;
import io.axoniq.axonserver.grpc.ErrorMessage;
import io.axoniq.axonserver.grpc.ErrorMessageOrBuilder;
import org.axonframework.axonserver.connector.query.AxonServerRemoteQueryHandlingException;
import org.axonframework.queryhandling.QueryExecutionException;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

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

        var duplicateItemIds = createPaymentQuery.getItemIds().stream().filter(item -> createPaymentQuery.getItemIds().stream().filter(i -> i.equals(item)).count() > 1).collect(Collectors.toSet());

        if (!duplicateItemIds.isEmpty()) {
            throw new ExceptionWithErrorCode("Duplicate items found with IDs: " + duplicateItemIds, 400);
        }

        var items = itemRepository.findByIds(createPaymentQuery.getItemIds());

        if(items.isEmpty()) {
            throw new ExceptionWithErrorCode("No items found", 404);
        }

        if(items.size() != createPaymentQuery.getItemIds().size()) {
            var notFoundItems = createPaymentQuery.getItemIds();
            notFoundItems.removeAll(items.stream().map(Item::getId).toList());
            throw new ExceptionWithErrorCode("Some items not found with IDs: " + notFoundItems , 404);
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
