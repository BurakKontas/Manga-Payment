package com.aburakkontas.manga_payment.api.controllers;

import com.aburakkontas.manga.common.payment.queries.GetUserCreditQuery;
import com.aburakkontas.manga.common.payment.queries.results.GetUserCreditQueryResult;
import com.aburakkontas.manga_payment.contracts.response.GetUserCreditResponse;
import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_payment.domain.primitives.Result;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserCreditController {

    private final QueryGateway queryGateway;

    public UserCreditController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/get")
    public ResponseEntity<Result<GetUserCreditResponse>> getLoggedInUserData(@RequestParam(required = false) UUID userId, Authentication authentication) {
        var executorId = UUID.fromString(authentication.getCredentials().toString());
        var ex = new ExceptionWithErrorCode("You are not authorized to view other users' data", 403);
        var role = authentication.getAuthorities().stream().findFirst().orElseThrow(() -> ex).getAuthority();

        if(userId == null) userId = executorId;
        else if(!role.equals("Admin")) throw ex;

        var query = new GetUserCreditQuery(userId);

        var result = queryGateway.query(query, GetUserCreditQueryResult.class).join();

        var response = new GetUserCreditResponse(result.getCredit(), result.getSuccessfulTransactions(), result.getFailedTransactions());

        return ResponseEntity.ok(Result.success(response));
    }
}
