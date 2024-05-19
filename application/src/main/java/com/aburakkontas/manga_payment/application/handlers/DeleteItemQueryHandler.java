package com.aburakkontas.manga_payment.application.handlers;

import com.aburakkontas.manga.common.payment.queries.DeleteItemQuery;
import com.aburakkontas.manga.common.payment.queries.results.DeleteItemQueryResult;
import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_payment.domain.repositories.ItemRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class DeleteItemQueryHandler {

    private final ItemRepository itemRepository;

    @Autowired
    public DeleteItemQueryHandler(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @QueryHandler
    public DeleteItemQueryResult handle(DeleteItemQuery query) {
        var isExists = itemRepository.existsById(query.getItemId());

        if(!isExists) throw new ExceptionWithErrorCode("Item not found", 404);

        itemRepository.deleteById(query.getItemId());

        var queryResult = new DeleteItemQueryResult();
        queryResult.setDeleted(true);
        queryResult.setItemId(query.getItemId());

        return queryResult;
    }
}
