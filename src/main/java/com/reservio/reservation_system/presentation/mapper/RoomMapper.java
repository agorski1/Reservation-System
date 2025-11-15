package com.reservio.reservation_system.presentation.mapper;

import com.reservio.reservation_system.infrastructure.entity.ReservationEntity;
import com.reservio.reservation_system.infrastructure.entity.RoomEntity;
import com.reservio.reservation_system.presentation.dto.room.AvailableRoomDto;
import com.reservio.reservation_system.presentation.dto.room.RoomDetailsDto;
import com.reservio.reservation_system.presentation.dto.room.RoomDto;
import com.reservio.reservation_system.presentation.dto.room.RoomSlotDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface RoomMapper {

    @Mapping(source = "rsvCheckInDate", target = "start")
    @Mapping(source = "rsvCheckOutDate", target = "end")
    @Mapping(source = "usr", target = "user")
    RoomSlotDto toRoomSlotDto(ReservationEntity reservation);
    List<RoomSlotDto> toRoomSlotDtoList(List<ReservationEntity> reservations);

    @Mapping(source = "id", target = "roomId")
    @Mapping(source = "rmNumber", target = "roomNumber")
    RoomDetailsDto toRoomDetailsDto(RoomEntity room);
    List<RoomDetailsDto> toRoomDetailsDtoList(List<RoomEntity> rooms);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "rmNumber", target = "number")
    @Mapping(source = "rt.rtName", target = "name")
    @Mapping(source = "rt.rtCapacity", target = "capacity")
    @Mapping(source = "rt.rtPricePerNight", target = "pricePerNight")
    @Mapping(source = "rmStatus", target = "status")
    RoomDto toRoomDto(RoomEntity room);

    List<RoomDto> toRoomDtoList(List<RoomEntity> rooms);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "rmNumber", target = "number")
    AvailableRoomDto toAvailableRoomDto(RoomEntity room);

    List<AvailableRoomDto> toAvailableRoomDtoList(List<RoomEntity> rooms);
}

