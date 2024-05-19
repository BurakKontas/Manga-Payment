package com.aburakkontas.manga_payment.api.controllers;

import com.aburakkontas.manga.common.payment.queries.*;
import com.aburakkontas.manga.common.payment.queries.results.*;
import com.aburakkontas.manga_payment.contracts.request.CreateItemRequest;
import com.aburakkontas.manga_payment.contracts.request.UpdateItemRequest;
import com.aburakkontas.manga_payment.contracts.response.*;
import com.aburakkontas.manga_payment.domain.primitives.Result;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/items")
public class ItemsController {

    private final QueryGateway queryGateway;

    @Autowired
    public ItemsController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @PostMapping("/create-item")
    public ResponseEntity<Result<CreateItemResponse>> createItem(@RequestBody CreateItemRequest request) {
        var query = CreateItemQuery.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .itemType(request.getItemType())
                .build();

        var result = queryGateway.query(query, CreateItemQueryResult.class).join();

        var response = new CreateItemResponse();
        response.setCreated(result.isCreated());
        response.setItemId(result.getItemId());

        return ResponseEntity.ok(Result.success(response));
    }

    @PutMapping("/update-item")
    public ResponseEntity<Result<UpdateItemResponse>> updateItem(@RequestBody UpdateItemRequest request) {
        var query = UpdateItemQuery.builder()
                .itemId(request.getItemId())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .itemType(request.getItemType())
                .build();

        var result = queryGateway.query(query, UpdateItemQueryResult.class).join();

        var response = new UpdateItemResponse();
        response.setUpdated(result.isUpdated());
        response.setItemId(result.getItemId());

        return ResponseEntity.ok(Result.success(response));
    }

    @DeleteMapping("/delete-item")
    public ResponseEntity<Result<DeleteItemResponse>> deleteItem(@RequestParam UUID itemId) {
        var query = DeleteItemQuery.builder()
                .itemId(itemId)
                .build();

        var result = queryGateway.query(query, DeleteItemQueryResult.class).join();

        var response = new DeleteItemResponse();
        response.setDeleted(result.isDeleted());
        response.setItemId(result.getItemId());

        return ResponseEntity.ok(Result.success(response));
    }

    @GetMapping("/get-item")
    public ResponseEntity<Result<GetItemResponse>> getItem(@RequestParam UUID itemId) {
        var query = GetItemQuery.builder()
                .itemId(itemId)
                .build();

        var result = queryGateway.query(query, GetItemQueryResult.class).join();

        var response = new GetItemResponse();
        response.setItemId(result.getItemId());
        response.setName(result.getName());
        response.setDescription(result.getDescription());
        response.setPrice(result.getPrice());
        response.setCategory(result.getCategory());
        response.setImageId(result.getImageId());
        response.setItemType(result.getItemType());

        return ResponseEntity.ok(Result.success(response));
    }

    @GetMapping("/get-all")
    public ResponseEntity<Result<GetAllItemsResponse>> getItems() {
        var query = GetAllItemsQuery.builder().build();

        var result = queryGateway.query(query, GetAllItemsQueryResult.class).join();

        var response = new GetAllItemsResponse();
        var items = new ArrayList<GetItemResponse>();
        for (var item : result.getItems()) {
            var itemResponse = new GetItemResponse();
            itemResponse.setItemId(item.getItemId());
            itemResponse.setName(item.getName());
            itemResponse.setDescription(item.getDescription());
            itemResponse.setPrice(item.getPrice());
            itemResponse.setCategory(item.getCategory());
            itemResponse.setImageId(item.getImageId());
            itemResponse.setItemType(item.getItemType());
            items.add(itemResponse);
        }
        response.setItems(items);

        return ResponseEntity.ok(Result.success(response));
    }
}
