package com.aburakkontas.manga_payment.contracts.response;

import lombok.Data;

import java.util.ArrayList;

@Data
public class GetAllItemsResponse {
    ArrayList<GetItemResponse> items;
}
