package com.reservio.reservation_system.domain.repository;

import com.reservio.reservation_system.infrastructure.entity.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentMethodDao extends JpaRepository<PaymentMethodEntity, Long> {

    Optional<PaymentMethodEntity> findByPmtmName(String pmtmName);
}
