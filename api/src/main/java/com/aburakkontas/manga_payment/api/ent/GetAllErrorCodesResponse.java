package com.aburakkontas.manga_payment.api.ent;

import lombok.Data;

import java.util.Map;

@Data
public class GetAllErrorCodesResponse {
    private Map<String, Integer> errorCodes;
}
