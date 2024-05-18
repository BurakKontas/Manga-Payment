package com.aburakkontas.manga_payment.application.handlers;

import com.aburakkontas.manga.common.payment.queries.UpdateItemQuery;
import com.aburakkontas.manga.common.payment.queries.results.UpdateItemQueryResult;
import com.aburakkontas.manga_payment.domain.repositories.ItemRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UpdateItemQueryHandler {

    private final ItemRepository itemRepository;

    @Autowired
    public UpdateItemQueryHandler(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @QueryHandler
    public UpdateItemQueryResult handle(UpdateItemQuery query) {
        var item = itemRepository.findById(query.getItemId()).orElseThrow();

        item.setName(query.getName());
        item.setPrice(query.getPrice());
        item.setCategory(query.getCategory());
        item.setDescription(query.getDescription());
        item.setItemType(query.getItemType());

        itemRepository.save(item);

        var queryResult = new UpdateItemQueryResult();
        queryResult.setUpdated(true);
        queryResult.setItemId(item.getId());

        return queryResult;
    }
}
