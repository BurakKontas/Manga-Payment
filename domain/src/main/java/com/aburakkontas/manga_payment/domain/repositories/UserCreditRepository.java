package com.aburakkontas.manga_payment.domain.repositories;

import com.aburakkontas.manga_payment.domain.entities.usercredit.UserCredit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserCreditRepository extends JpaRepository<UserCredit, UUID> {
}
