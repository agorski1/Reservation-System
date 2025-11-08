package com.reservio.reservation_system.domain.repository;

import com.reservio.reservation_system.infrastructure.entity.ReservationStatusEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationStatusDao extends JpaRepository<ReservationStatusEntity, Long> {

    Optional<ReservationStatusEntity> findByRsvsName(String rsvsName);
}