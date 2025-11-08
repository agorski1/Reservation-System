package com.reservio.reservation_system.domain.repository;
import com.reservio.reservation_system.infrastructure.entity.PaymentStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentStatusDao extends JpaRepository<PaymentStatusEntity, Long> {
    Optional<PaymentStatusEntity> findByPmtsName(String pmtsName);
}