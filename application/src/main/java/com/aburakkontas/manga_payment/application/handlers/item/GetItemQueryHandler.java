package com.aburakkontas.manga_payment.application.handlers.item;

import com.aburakkontas.manga.common.payment.queries.GetItemQuery;
import com.aburakkontas.manga.common.payment.queries.results.GetItemQueryResult;
import com.aburakkontas.manga_payment.application.utils.ItemMapper;
import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_payment.domain.repositories.ItemRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        var queryResult = ItemMapper.mapToQueryResult(item);

        return queryResult;
    }
}
