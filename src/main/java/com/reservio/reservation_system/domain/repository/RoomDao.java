package com.reservio.reservation_system.domain.repository;

import com.reservio.reservation_system.infrastructure.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomDao extends JpaRepository<RoomEntity, Long> {
    RoomEntity findRoomEntitiesById(Long id);
    List<RoomEntity> findAllByOrderByRmNumber();
}
