package com.reservio.reservation_system.domain.service;

import com.reservio.reservation_system.domain.repository.RoomTypeDao;
import com.reservio.reservation_system.infrastructure.entity.RoomTypeEntity;
import com.reservio.reservation_system.presentation.dto.room.AvailableRoomTypeDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
@AllArgsConstructor
public class RoomTypeService {
    private final RoomTypeDao roomTypeDao;

    public List<AvailableRoomTypeDto> findAvailableRoomTypes(
            LocalDate from,
            LocalDate to,
            Integer capacity,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            List<String> amenities
    ) {
        List<RoomTypeEntity> roomTypes = roomTypeDao.findAvailableRoomTypes(
                from,
                to,
                capacity,
                minPrice,
                maxPrice,
                amenities);



        return null;
    }
}

