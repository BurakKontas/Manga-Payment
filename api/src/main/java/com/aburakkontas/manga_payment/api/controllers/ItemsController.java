package com.aburakkontas.manga_payment.api.controllers;

import com.aburakkontas.manga.common.payment.queries.*;
import com.aburakkontas.manga.common.payment.queries.results.*;
import com.aburakkontas.manga_payment.contracts.request.CreateItemRequest;
import com.aburakkontas.manga_payment.contracts.request.UpdateItemRequest;
import com.aburakkontas.manga_payment.contracts.response.*;
import com.aburakkontas.manga_payment.domain.primitives.Result;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/items")
public class ItemsController {

    private final QueryGateway queryGateway;
    private final Environment env;

    @Autowired
    public ItemsController(QueryGateway queryGateway, Environment env) {
        this.queryGateway = queryGateway;
        this.env = env;
    }

    @PostMapping("/create-item")
    public ResponseEntity<Result<CreateItemResponse>> createItem(@RequestBody CreateItemRequest request) {
        var query = CreateItemQuery.builder()
                .imageId(request.getImageId())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .itemType(request.getItemType())
                .build();

        var result = queryGateway.query(query, CreateItemQueryResult.class).join();

        var imageUri = generateImageUri(request.getImageId());

        var response = new CreateItemResponse();
        response.setCreated(result.isCreated());
        response.setItemId(result.getItemId());
        response.setImageUri(imageUri);

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
                .imageId(request.getImageId())
                .build();

        var result = queryGateway.query(query, UpdateItemQueryResult.class).join();

        var imageUri = generateImageUri(request.getImageId());

        var response = new UpdateItemResponse();
        response.setUpdated(result.isUpdated());
        response.setItemId(result.getItemId());
        response.setImageUri(imageUri);

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

        var response = getItemResponse(result);

        return ResponseEntity.ok(Result.success(response));
    }

    @GetMapping("/get-all")
    public ResponseEntity<Result<GetAllItemsResponse>> getItems() {
        var query = GetAllItemsQuery.builder().build();

        var result = queryGateway.query(query, GetAllItemsQueryResult.class).join();

        var response = new GetAllItemsResponse();
        var items = new ArrayList<GetItemResponse>();
        for (var item : result.getItems()) {
            var itemResponse = getItemResponse(item);
            items.add(itemResponse);
        }
        response.setItems(items);

        return ResponseEntity.ok(Result.success(response));
    }

    private GetItemResponse getItemResponse(GetItemQueryResult item) {
        var imageUri = generateImageUri(item.getImageId());

        var response = new GetItemResponse();
        response.setItemId(item.getItemId());
        response.setName(item.getName());
        response.setDescription(item.getDescription());
        response.setPrice(item.getPrice());
        response.setCategory(item.getCategory());
        response.setImageId(item.getImageId());
        response.setItemType(item.getItemType());
        response.setImageUri(imageUri);
        return response;
    }

    private String generateImageUri(UUID imageId) {
        return MessageFormat.format("{0}/{1}/{2}", env.getProperty("cdn.uri"), env.getProperty("cdn.image.path"), imageId);
    }
}
