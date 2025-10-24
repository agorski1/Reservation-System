package com.reservio.reservation_system.presentation.mapper;

import com.reservio.reservation_system.infrastructure.entity.RoomTypeEntity;
import com.reservio.reservation_system.presentation.dto.room.AvailableRoomTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper {
    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "name", source = "entity.rtName")
    @Mapping(target = "capacity", source = "entity.rtCapacity")
    @Mapping(target = "pricePerNight", source = "entity.rtPricePerNight")
    @Mapping(target = "totalPrice", expression = "java(calculateTotalPrice(entity, from, to)")
    @Mapping(target = "amenities", expression = "java(mapAmenities(entity))")
    AvailableRoomTypeDto toAvailableRoomTypeDto(RoomTypeEntity roomTypeEntity, LocalDateTime from, LocalDateTime to);
    List<AvailableRoomTypeDto> toAvailableRoomTypeDtos(List<RoomTypeEntity> roomTypeEntities, LocalDateTime from, LocalDateTime to);

    default BigDecimal calculateTotalPrice(RoomTypeEntity entity,
                                           LocalDateTime from,
                                           LocalDateTime to) {
        if(entity.getRtPricePerNight() == null || from == null || to == null) {
           return BigDecimal.ZERO;
        }
        long days =  ChronoUnit.DAYS.between(from, to);
        return entity.getRtPricePerNight().multiply(BigDecimal.valueOf(days));
    }

    default List<AvailableRoomTypeDto> mapAmenities(RoomTypeEntity entity) {

    }
}
