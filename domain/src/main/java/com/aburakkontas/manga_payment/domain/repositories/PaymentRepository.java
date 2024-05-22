package com.aburakkontas.manga_payment.domain.repositories;

import com.aburakkontas.manga_payment.domain.entities.payment.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    @Query("SELECT p FROM Payment p WHERE " +
            "(:from IS NULL OR p.paymentDate >= :from) AND " +
            "(:to IS NULL OR p.paymentDate <= :to) AND " +
            "(p.user.userId = :userId) " +
            "ORDER BY " +
            "CASE WHEN :sort = 'paymentDate' THEN p.paymentDate ELSE null END, " +
            "CASE WHEN :order = 'ASC' THEN p.paymentDate ELSE null END ASC, " +
            "CASE WHEN :order = 'DESC' THEN p.paymentDate ELSE null END DESC")
    Page<Payment> findPaymentsWithFilters(@Param("from") ZonedDateTime from,
                                          @Param("to") ZonedDateTime to,
                                          @Param("userId") UUID userId,
                                          @Param("sort") String sort,
                                          @Param("order") String order,
                                          Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE p.user.userId = :userId ORDER BY p.paymentDate DESC")
    Page<Payment> findAllByUserId(UUID userId, Pageable pageable);
}
