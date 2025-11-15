package com.reservio.reservation_system.domain.repository;

import com.reservio.reservation_system.infrastructure.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleDao extends JpaRepository<UserRoleEntity, Long> {
    Optional<UserRoleEntity> findByUrName(String userRole);
}
