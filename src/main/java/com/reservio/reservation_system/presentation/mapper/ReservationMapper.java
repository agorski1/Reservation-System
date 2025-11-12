package com.reservio.reservation_system.presentation.mapper;

import com.reservio.reservation_system.infrastructure.entity.ReservationEntity;
import com.reservio.reservation_system.presentation.dto.reservation.ReservationDto;
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

    @Mapping(source = "id", target = "id")
    @Mapping(source = "usr.usrFirstName", target = "firstName")
    @Mapping(source = "usr.usrLastName", target = "lastName")
    @Mapping(source = "usr.usrEmail", target = "email")
    @Mapping(source = "usr.usrPhoneNumber", target = "phoneNumber")
    @Mapping(source = "rm.rt.rtName", target = "roomType")
    @Mapping(source = "rsvGuestCount", target = "guestCount")
    @Mapping(source = "rsvs.rsvsName", target = "status")
    @Mapping(source = "rsvCheckInDate", target = "checkInDate")
    @Mapping(source = "rsvCheckOutDate", target = "checkOutDate")
    ReservationDto toReservationDto(ReservationEntity entity);

    List<ReservationDto> toReservationDtos(List<ReservationEntity> entities);
}
