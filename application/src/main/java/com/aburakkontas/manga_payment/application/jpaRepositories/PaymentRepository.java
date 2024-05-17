package com.aburakkontas.manga_payment.application.jpaRepositories;

import com.aburakkontas.manga_payment.domain.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    @Query("SELECT p FROM Payment p WHERE " +
            "(:from IS NULL OR p.paymentDate >= :from) AND " +
            "(:to IS NULL OR p.paymentDate <= :to) AND " +
            "(p.userId = :userId) " +
            "ORDER BY " +
            "CASE WHEN :sort = 'paymentDate' THEN p.paymentDate ELSE null END, " +
            "CASE WHEN :order = 'ASC' THEN p.paymentDate ELSE null END ASC, " +
            "CASE WHEN :order = 'DESC' THEN p.paymentDate ELSE null END DESC")
    Page<Payment> findPaymentsWithFilters(ZonedDateTime from,
                                          ZonedDateTime to,
                                          UUID userId,
                                          String sort,
                                          String order,
                                          Pageable pageable);
}
