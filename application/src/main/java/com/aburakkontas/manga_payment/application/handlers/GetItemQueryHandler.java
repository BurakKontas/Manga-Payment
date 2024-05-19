package com.aburakkontas.manga_payment.application.handlers;

import com.aburakkontas.manga.common.payment.queries.GetItemQuery;
import com.aburakkontas.manga.common.payment.queries.results.GetItemQueryResult;
import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_payment.domain.repositories.ItemRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class GetItemQueryHandler {

    private final ItemRepository itemRepository;

    @Autowired
    public GetItemQueryHandler(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @QueryHandler
    public GetItemQueryResult handle(GetItemQuery query) {
        var item = itemRepository.findById(query.getItemId()).orElseThrow(
                () -> new ExceptionWithErrorCode("Item not found", 404)
        );

        var queryResult = new GetItemQueryResult();
        queryResult.setItemId(item.getId());
        queryResult.setName(item.getName());
        queryResult.setPrice(item.getPrice());
        queryResult.setCategory(item.getCategory());
        queryResult.setDescription(item.getDescription());
        queryResult.setItemType(item.getItemType());
        queryResult.setImageId(item.getImageId());

        return queryResult;
    }
}
