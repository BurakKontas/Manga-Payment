package com.aburakkontas.manga_payment.api.controllers;

import com.aburakkontas.manga.common.payment.queries.CreateItemQuery;
import com.aburakkontas.manga.common.payment.queries.results.CreateItemQueryResult;
import com.aburakkontas.manga_payment.contracts.request.CreateItemRequest;
import com.aburakkontas.manga_payment.contracts.request.CreatePaymentRequest;
import com.aburakkontas.manga_payment.contracts.response.CreateItemResponse;
import com.aburakkontas.manga_payment.contracts.response.CreatePaymentResponse;
import com.aburakkontas.manga_payment.domain.primitives.Result;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/items")
public class ItemsController {

    private final QueryGateway queryGateway;

    @Autowired
    public ItemsController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    //crud operations for item
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
    public void updateItem() {
        //update item
    }

    @DeleteMapping("/delete-item")
    public void deleteItem() {
        //delete item
    }

    @GetMapping("/get-item")
    public void getItem(@RequestParam UUID itemId) {
        //get item
    }

    @GetMapping("/get-all")
    public void getItems() {
        //get items
    }
}
