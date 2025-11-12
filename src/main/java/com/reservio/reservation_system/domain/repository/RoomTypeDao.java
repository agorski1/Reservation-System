package com.reservio.reservation_system.domain.repository;

import com.reservio.reservation_system.infrastructure.entity.RoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomTypeDao extends JpaRepository<RoomTypeEntity, Long> {
    @Query(
            "SELECT DISTINCT r.rt \n" +
                    "FROM RoomEntity r \n" +
                    "WHERE r.rmStatus = 'ACTIVE'\n" +
                    "    AND NOT EXISTS (\n" +
                    "        SELECT res \n" +
                    "        FROM ReservationEntity res \n" +
                    "        WHERE res.rm = r \n" +
                    "            AND res.rsvCheckInDate < :checkOut\n" +
                    "            AND res.rsvCheckOutDate > :checkIn\n" +
                    "    )\n" +
                    "    AND (:capacity IS NULL OR r.rt.rtCapacity IN :capacity)\n" +
                    "    AND (:minPrice IS NULL OR r.rt.rtPricePerNight >= :minPrice)\n" +
                    "    AND (:maxPrice IS NULL OR r.rt.rtPricePerNight <= :maxPrice)\n" +
                    "    AND (:amenities IS NULL OR NOT EXISTS (\n" +
                    "        SELECT a2.amnCode \n" +
                    "        FROM AmenityEntity a2 \n" +
                    "        WHERE a2.amnCode IN :amenities \n" +
                    "            AND NOT EXISTS (\n" +
                    "                SELECT rta \n" +
                    "                FROM RoomTypeAmenityEntity rta \n" +
                    "                JOIN rta.amn a \n" +
                    "                WHERE rta.rt = r.rt \n" +
                    "                    AND a.amnCode = a2.amnCode\n" +
                    "            )\n" +
                    "    ))")
    List<RoomTypeEntity> findAvailableRoomTypes(@Param("checkIn") LocalDateTime checkIn,
                                                @Param("checkOut") LocalDateTime checkOut,
                                                @Param("capacity") List<Integer> capacity,
                                                @Param("minPrice") BigDecimal minPrice,
                                                @Param("maxPrice") BigDecimal maxPrice,
                                                @Param("amenities")  List<String> amenities);

    RoomTypeEntity findFirstByRtName(String name);
}