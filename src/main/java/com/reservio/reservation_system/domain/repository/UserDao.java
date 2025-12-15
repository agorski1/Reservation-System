package com.reservio.reservation_system.domain.repository;

import com.reservio.reservation_system.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsrEmail(String email);
    boolean existsByUsrEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.ur.urName = 'Employee'")
    List<UserEntity> findAllEmployees();

}
