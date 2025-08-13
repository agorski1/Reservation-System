package com.reservio.reservation_system.domain.repository;

import com.reservio.reservation_system.infrastructure.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleDao extends JpaRepository<UserRoleEntity, Long> {
    UserRoleEntity findByUrName(String userRole);
}
