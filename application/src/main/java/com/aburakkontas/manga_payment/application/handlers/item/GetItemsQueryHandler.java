package com.aburakkontas.manga_payment.application.handlers.item;

import com.aburakkontas.manga.common.payment.queries.GetAllItemsQuery;
import com.aburakkontas.manga.common.payment.queries.results.GetAllItemsQueryResult;
import com.aburakkontas.manga.common.payment.queries.results.GetItemQueryResult;
import com.aburakkontas.manga_payment.application.utils.ItemMapper;
import com.aburakkontas.manga_payment.domain.repositories.ItemRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
            var queryResult = ItemMapper.mapToQueryResult(item);
            itemArray.add(queryResult);
        }

        var queryResult = new GetAllItemsQueryResult();
        queryResult.setItems(itemArray);

        return queryResult;
    }
}
