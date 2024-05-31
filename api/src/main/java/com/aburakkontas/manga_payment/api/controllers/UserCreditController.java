package com.aburakkontas.manga_payment.api.controllers;

import com.aburakkontas.manga.common.payment.queries.GetUserCreditQuery;
import com.aburakkontas.manga.common.payment.queries.results.GetUserCreditQueryResult;
import com.aburakkontas.manga_payment.contracts.response.GetUserCreditResponse;
import com.aburakkontas.manga_payment.domain.primitives.Result;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserCreditController {

    private QueryGateway queryGateway;

    public UserCreditController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/get")
    public ResponseEntity<Result<GetUserCreditResponse>> getLoggedInUserData(Authentication authentication) {
        var userId = UUID.fromString(authentication.getCredentials().toString());

        var query = new GetUserCreditQuery(userId);

        var result = queryGateway.query(query, GetUserCreditQueryResult.class).join();

        var response = new GetUserCreditResponse(result.getCredit(), result.getPayments(), result.getSuccessfulTransactions(), result.getFailedTransactions());

        return ResponseEntity.ok(Result.success(response));
    }
}
