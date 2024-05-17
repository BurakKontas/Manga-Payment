package com.aburakkontas.manga_payment.api.controllers;

import com.aburakkontas.manga_payment.contracts.request.CreatePaymentRequest;
import com.aburakkontas.manga_payment.contracts.response.CreatePaymentResponse;
import com.aburakkontas.manga_payment.domain.primitives.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v1/items")
public class ItemsController {

    //crud operations for item
    @PostMapping("/create-item")
    public ResponseEntity<Result<CreatePaymentResponse>> createItem(@RequestBody CreatePaymentRequest request) {
        //create item
        return ResponseEntity.ok(Result.success(new CreatePaymentResponse()));
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
    public void getItem() {
        //get item
    }

    @GetMapping("/get-all")
    public void getItems() {
        //get items
    }
}
