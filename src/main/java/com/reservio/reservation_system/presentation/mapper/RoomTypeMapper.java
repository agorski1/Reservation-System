package com.reservio.reservation_system.presentation.mapper;

import com.reservio.reservation_system.infrastructure.entity.AmenityEntity;
import com.reservio.reservation_system.infrastructure.entity.RoomTypeAmenityEntity;
import com.reservio.reservation_system.infrastructure.entity.RoomTypeEntity;
import com.reservio.reservation_system.presentation.dto.room.AvailableRoomTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface RoomTypeMapper {
    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "name", source = "entity.rtName")
    @Mapping(target = "capacity", source = "entity.rtCapacity")
    @Mapping(target = "pricePerNight", source = "entity.rtPricePerNight")
    @Mapping(target = "totalPrice", source = "totalPrice")
    @Mapping(target = "amenities", source = "entity.roomTypeAmenities", qualifiedByName = "mapAmenityNames")
    AvailableRoomTypeDto toAvailableRoomTypeDto(RoomTypeEntity entity, BigDecimal totalPrice);

    default List<AvailableRoomTypeDto> toAvailableRoomTypeDtos(
            List<RoomTypeEntity> roomTypeEntities,
            Map<Long, BigDecimal> totalPricesById) {
        if (roomTypeEntities == null || totalPricesById == null) {
            return Collections.emptyList();
        }
        return roomTypeEntities.stream()
                .filter(entity -> entity.getId() != null) // Uniknij null ID
                .map(entity -> {
                    BigDecimal totalPrice = totalPricesById.getOrDefault(entity.getId(), BigDecimal.ZERO);
                    return toAvailableRoomTypeDto(entity, totalPrice);
                })
                .collect(Collectors.toList());
    }

    @Named("mapAmenityNames")
    default List<String> mapAmenityNames(Set<RoomTypeAmenityEntity> roomTypeAmenities) {
        if (roomTypeAmenities == null) {
            return Collections.emptyList();
        }
        return roomTypeAmenities.stream()
                .map(RoomTypeAmenityEntity::getAmn)
                .filter(amn -> amn != null && amn.getAmnName() != null)
                .map(AmenityEntity::getAmnName)
                .collect(Collectors.toList());
    }


}
