package com.aburakkontas.manga_payment.application.handlers.item;

import com.aburakkontas.manga.common.payment.queries.CreateItemQuery;
import com.aburakkontas.manga.common.payment.queries.results.CreateItemQueryResult;
import com.aburakkontas.manga_payment.domain.entities.item.Item;
import com.aburakkontas.manga_payment.domain.repositories.ItemRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateItemQueryHandler {

    private final ItemRepository itemRepository;

    @Autowired
    public CreateItemQueryHandler(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @QueryHandler
    public CreateItemQueryResult handle(CreateItemQuery query) {

        var item = new Item();
        item.setName(query.getName());
        item.setPrice(query.getPrice());
        item.setPrice(query.getPrice());
        item.setCategory(query.getCategory());
        item.setDescription(query.getDescription());
        item.setItemType(query.getItemType());
        item.setImageId(query.getImageId());

        var newItem = itemRepository.save(item);

        var queryResult = new CreateItemQueryResult();
        queryResult.setCreated(true);
        queryResult.setItemId(newItem.getId());

        return queryResult;
    }
}
