package com.reservio.reservation_system.domain.repository;

import com.reservio.reservation_system.infrastructure.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface PaymentDao extends JpaRepository<PaymentEntity, Long> {

    @Query("SELECT COALESCE(SUM(p.pmtAmount), 0)\n" +
            "    FROM PaymentEntity p\n" +
            "    WHERE p.rsv.id = :reservationId\n" +
            "      AND p.pmts.pmtsName = 'Paid" +
            "'\n")
    BigDecimal getPaidAmountReservation(@Param("reservationId") Long reservationId);

    @Query("""
           SELECT COALESCE(SUM(p.pmtAmount), 0)
           FROM PaymentEntity p
           JOIN p.pmts s
           WHERE s.pmtsName = 'PAID'
             AND p.pmtDate >= :startOfDay
             AND p.pmtDate < :endOfDay
           """)
    BigDecimal findPaidRevenueForToday(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}
