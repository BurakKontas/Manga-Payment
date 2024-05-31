package com.aburakkontas.manga_payment.application.handlers.usercredit;

import com.aburakkontas.manga.common.payment.queries.GetUserCreditQuery;
import com.aburakkontas.manga.common.payment.queries.results.GetUserCreditQueryResult;
import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_payment.domain.repositories.UserCreditRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class GetUserCreditQueryHandler {

    private final UserCreditRepository userCreditRepository;

    public GetUserCreditQueryHandler(UserCreditRepository userCreditRepository) {
        this.userCreditRepository = userCreditRepository;
    }

    @QueryHandler
    public GetUserCreditQueryResult handle(GetUserCreditQuery getUserCreditQuery) {
        var user = this.userCreditRepository.findById(getUserCreditQuery.getUserId()).orElseThrow(() -> new ExceptionWithErrorCode("User not found", 404));

        return new GetUserCreditQueryResult(
                user.getCredit(),
                user.getSuccessfulTransactions(),
                user.getFailedTransactions()
        );
    }
}
