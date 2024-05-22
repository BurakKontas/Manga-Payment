package com.aburakkontas.manga_payment.application.utils;

import com.aburakkontas.manga.common.payment.queries.results.GetItemQueryResult;
import com.aburakkontas.manga_payment.domain.entities.item.Item;

public class ItemMapper {

    public static GetItemQueryResult mapToQueryResult(Item item) {
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
