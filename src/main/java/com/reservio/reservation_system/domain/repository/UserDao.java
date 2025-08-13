package com.reservio.reservation_system.domain.repository;

import com.reservio.reservation_system.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsrEmail(String email);
    boolean existsByUsrEmail(String email);
}
