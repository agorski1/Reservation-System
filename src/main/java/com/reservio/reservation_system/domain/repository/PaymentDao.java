package com.reservio.reservation_system.domain.repository;

import com.reservio.reservation_system.infrastructure.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface PaymentDao extends JpaRepository<PaymentEntity, Long> {

    @Query("SELECT COALESCE(SUM(p.pmtAmount), 0)\n" +
            "    FROM PaymentEntity p\n" +
            "    WHERE p.rsv.id = :reservationId\n" +
            "      AND p.pmts.pmtsName = 'PAID'\n")
    BigDecimal getPaidAmountReservation(@Param("reservationId") Long reservationId);
}
