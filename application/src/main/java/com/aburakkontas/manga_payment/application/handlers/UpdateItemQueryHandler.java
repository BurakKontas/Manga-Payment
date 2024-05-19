package com.aburakkontas.manga_payment.application.handlers;

import com.aburakkontas.manga.common.payment.queries.UpdateItemQuery;
import com.aburakkontas.manga.common.payment.queries.results.UpdateItemQueryResult;
import com.aburakkontas.manga_payment.domain.repositories.ItemRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UpdateItemQueryHandler {

    private final ItemRepository itemRepository;

    @Autowired
    public UpdateItemQueryHandler(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @QueryHandler
    public UpdateItemQueryResult handle(UpdateItemQuery query) {
        var item = itemRepository.findById(query.getItemId()).orElseThrow();

        if(query.getName() != null) item.setName(query.getName());
        if(query.getPrice() != null) item.setPrice(query.getPrice());
        if(query.getCategory() != null) item.setCategory(query.getCategory());
        if(query.getDescription() != null) item.setDescription(query.getDescription());
        if(query.getItemType() != null) item.setItemType(query.getItemType());
        if(query.getImageId() != null) item.setImageId(query.getImageId());

        itemRepository.save(item);

        var queryResult = new UpdateItemQueryResult();
        queryResult.setUpdated(true);
        queryResult.setItemId(item.getId());

        return queryResult;
    }
}
