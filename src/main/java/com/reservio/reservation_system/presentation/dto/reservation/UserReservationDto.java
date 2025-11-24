package com.reservio.reservation_system.presentation.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserReservationDto {
    private Long reservationId;
    private Long roomNumber;
    private String roomType;
    private LocalDateTime from;
    private LocalDateTime to;
    private String status;
    private BigDecimal totalPrice;
    private BigDecimal paidAmount;
    private BigDecimal remainingAmount;
}
