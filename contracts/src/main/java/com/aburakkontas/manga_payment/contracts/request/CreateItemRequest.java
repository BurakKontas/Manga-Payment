package com.aburakkontas.manga_payment.contracts.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CreateItemRequest {
    private String name;
    private String description;
    private Double price;
    private String category;
    private ImageData image;
    private String itemType;

    @Data
    @AllArgsConstructor
    private static class ImageData {
        private String name;
        private byte[] data;
        private String type;
    }
}
