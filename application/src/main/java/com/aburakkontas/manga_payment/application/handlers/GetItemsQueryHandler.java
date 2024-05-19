package com.aburakkontas.manga_payment.application.handlers;

import com.aburakkontas.manga.common.payment.queries.GetAllItemsQuery;
import com.aburakkontas.manga.common.payment.queries.results.GetAllItemsQueryResult;
import com.aburakkontas.manga.common.payment.queries.results.GetItemQueryResult;
import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_payment.domain.repositories.ItemRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Component
public class GetItemsQueryHandler {

    private final ItemRepository itemRepository;

    @Autowired
    public GetItemsQueryHandler(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @QueryHandler
    public GetAllItemsQueryResult handle(GetAllItemsQuery query) {
        var items = itemRepository.findAll();

        var itemArray = new ArrayList<GetItemQueryResult>();
        for (var item : items) {
            var queryResult = new GetItemQueryResult();
            queryResult.setItemId(item.getId());
            queryResult.setName(item.getName());
            queryResult.setPrice(item.getPrice());
            queryResult.setCategory(item.getCategory());
            queryResult.setDescription(item.getDescription());
            queryResult.setItemType(item.getItemType());
            queryResult.setImageId(item.getImageId());
            itemArray.add(queryResult);
        }

        var queryResult = new GetAllItemsQueryResult();
        queryResult.setItems(itemArray);

        return queryResult;
    }
}
