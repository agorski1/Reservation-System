package com.reservio.reservation_system.presentation.mapper;

import com.reservio.reservation_system.infrastructure.entity.ReservationEntity;
import com.reservio.reservation_system.presentation.dto.reservation.RoomReservationDto;
import com.reservio.reservation_system.presentation.dto.reservation.UserReservationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ReservationMapper {
    @Mapping(source = "id", target = "reservationId")
    @Mapping(source = "rm.rmNumber", target = "roomNumber")
    @Mapping(source = "rsvCheckInDate", target = "from")
    @Mapping(source = "rsvCheckOutDate", target = "to")
    UserReservationDto toUserReservationDto(ReservationEntity entity);
    List<UserReservationDto> toUserReservationDtos(List<ReservationEntity> entities);

    @Mapping(source = "id", target = "reservationId")
    @Mapping(source = "rsvCheckInDate", target = "from")
    @Mapping(source = "rsvCheckOutDate", target = "to")
    @Mapping(source = "usr", target = "user")
    RoomReservationDto toRoomReservationDto(ReservationEntity entity);
    List<RoomReservationDto> toRoomReservationDtos(List<ReservationEntity> entities);
}
