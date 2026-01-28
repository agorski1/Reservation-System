package com.reservio.reservation_system.presentation.mapper;

import com.reservio.reservation_system.infrastructure.entity.PaymentEntity;
import com.reservio.reservation_system.infrastructure.entity.ReservationEntity;
import com.reservio.reservation_system.presentation.dto.reservation.RoomReservationResponseDto;
import com.reservio.reservation_system.presentation.dto.reservation.UserReservationDto;
import com.reservio.reservation_system.presentation.dto.reservation.ReservationDto;
import com.reservio.reservation_system.presentation.dto.reservation.RoomReservationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class) public interface ReservationMapper {
    @Mapping(source = "id", target = "reservationId")
    @Mapping(source = "rm.rmNumber", target = "roomNumber")
    @Mapping(source = "rsvCheckInDate", target = "from")
    @Mapping(source = "rsvCheckOutDate", target = "to")
    @Mapping(source = "rsvs.rsvsName", target = "status")
    @Mapping(target = "totalPrice",
            expression = "java(calculateTotalPrice(entity))")
    @Mapping(target = "paidAmount",
            expression = "java(calculatePaidAmount(entity))")
    @Mapping(target = "remainingAmount",
            expression = "java(calculateRemaining(entity))")
    UserReservationDto toUserReservationDto(ReservationEntity entity);

    List<UserReservationDto> toUserReservationDtos(
            List<ReservationEntity> entities);

    @Mapping(source = "id", target = "reservationId")
    @Mapping(source = "rsvCheckInDate", target = "from")
    @Mapping(source = "rsvCheckOutDate", target = "to")
    @Mapping(source = "usr", target = "user")
    RoomReservationDto toRoomReservationDto(ReservationEntity entity);

    List<RoomReservationDto> toRoomReservationDtos(
            List<ReservationEntity> entities);

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

    List<ReservationDto> toReservationDtos(
            List<ReservationEntity> entities);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "rm.id", target = "roomId")
    @Mapping(source = "rsvGuestCount", target = "guestCount")
    @Mapping(source = "rsvCheckInDate", target = "from")
    @Mapping(source = "rsvCheckOutDate", target = "to")
    @Mapping(source = "rsvs.rsvsName", target = "status")
    RoomReservationResponseDto toRoomReservationResponseDto(
            ReservationEntity entity);

    default BigDecimal calculateTotalPrice(ReservationEntity r) {
        if (r == null || r.getRm() == null || r.getRm().getRt() == null) {
            return BigDecimal.ZERO;
        }
        long days = ChronoUnit.DAYS.between(
                r.getRsvCheckInDate().toLocalDate(),
                r.getRsvCheckOutDate().toLocalDate()
        );
        return r.getRm().getRt().getRtPricePerNight()
                .multiply(BigDecimal.valueOf(days));
    }

    default BigDecimal calculatePaidAmount(ReservationEntity r) {
        if (r == null || r.getPayments() == null) {
            return BigDecimal.ZERO;
        }
        return r.getPayments().stream()
                .filter(p -> p.getPmts() != null
                        && "PAID".equals(p.getPmts().getPmtsName()))
                .map(PaymentEntity::getPmtAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    default BigDecimal calculateRemaining(ReservationEntity r) {
        return calculateTotalPrice(r).subtract(calculatePaidAmount(r));
    }
}
