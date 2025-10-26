package com.reservio.reservation_system.domain.service;

import com.reservio.reservation_system.domain.repository.RoomTypeDao;
import com.reservio.reservation_system.infrastructure.entity.RoomTypeEntity;
import com.reservio.reservation_system.presentation.dto.room.AvailableRoomTypeDto;
import com.reservio.reservation_system.presentation.mapper.RoomTypeMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class RoomTypeService {
    private final RoomTypeDao roomTypeDao;
    private final RoomTypeMapper roomTypeMapper;
    private static final Logger log = LoggerFactory.getLogger(RoomTypeService.class);

    public List<AvailableRoomTypeDto> findAvailableRoomTypes(
            LocalDateTime from,
            LocalDateTime to,
            List<Integer> capacity,
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

        Map<Long, BigDecimal> totalPricesById = roomTypes.stream()
                .collect(Collectors.toMap(
                        RoomTypeEntity::getId,
                        entity -> calculateTotalPrice(entity, from, to)
                ));
        return roomTypeMapper.toAvailableRoomTypeDtos(roomTypes, totalPricesById);
    }

    private BigDecimal calculateTotalPrice(RoomTypeEntity entity, LocalDateTime from, LocalDateTime to) {
        if (entity == null || entity.getRtPricePerNight() == null || from == null || to == null || from.isAfter(to)) {
            log.warn("Invalid data for total price: entityId={}, price={}, from={}, to={}",
                    entity != null ? entity.getId() : null,
                    entity != null ? entity.getRtPricePerNight() : null, from, to);
            return BigDecimal.ZERO;
        }
        long days = ChronoUnit.DAYS.between(from, to);
        if (days <= 0) {
            log.warn("Invalid date range: days={}", days);
            return BigDecimal.ZERO;
        }
        BigDecimal totalPrice = entity.getRtPricePerNight().multiply(BigDecimal.valueOf(days));
        log.info("Calculated total price for entityId={}: {}", entity.getId(), totalPrice);
        return totalPrice;
    }
//    private BigDecimal calculateTotalPrice(RoomTypeEntity entity,
//                                           LocalDate from,
//                                           LocalDate to) {
//        if (entity.getRtPricePerNight() == null || from == null || to == null) {
//            return BigDecimal.ZERO;
//        }
//        long days = ChronoUnit.DAYS.between(from, to);
//        return entity.getRtPricePerNight().multiply(BigDecimal.valueOf(days));
//    }
}

