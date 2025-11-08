package com.reservio.reservation_system.domain.repository;

import com.reservio.reservation_system.infrastructure.entity.RoomEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomDao extends JpaRepository<RoomEntity, Long> {
    RoomEntity findRoomEntitiesById(Long id);
    List<RoomEntity> findAllByOrderByRmNumber();
    List<RoomEntity> findAll(Specification<RoomEntity> spec);

    @Query("""
    SELECT r FROM RoomEntity r
    WHERE r.rt.id = :roomTypeId
    AND NOT EXISTS (
        SELECT 1 FROM ReservationEntity res
        WHERE res.rm.id = r.id
        AND res.rsvCheckInDate < :to
        AND res.rsvCheckOutDate > :from
    )
""")
    List<RoomEntity> findAvailableRooms(
            @Param("roomTypeId") Long roomTypeId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
