package com.aburakkontas.manga_payment.application.handlers.usercredit;

import com.aburakkontas.manga.common.payment.queries.HasCreditQuery;
import com.aburakkontas.manga.common.payment.queries.results.HasCreditQueryResult;
import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_payment.domain.repositories.UserCreditRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class HasCreditQueryHandler {

    private final UserCreditRepository userCreditRepository;

    public HasCreditQueryHandler(UserCreditRepository userCreditRepository) {
        this.userCreditRepository = userCreditRepository;
    }

    @QueryHandler
    public HasCreditQueryResult handle(HasCreditQuery hasCreditQuery) {
        var userId = hasCreditQuery.getUserId();

        var user = userCreditRepository.findById(userId).orElseThrow(() -> new ExceptionWithErrorCode("User not found", 404));

        var hasCredit = user.hasCredit(hasCreditQuery.getCredit());

        return new HasCreditQueryResult(hasCredit, user.getCredit());
    }
}
