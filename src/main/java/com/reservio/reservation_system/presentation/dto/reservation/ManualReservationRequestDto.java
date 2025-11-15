package com.reservio.reservation_system.presentation.dto.reservation;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ManualReservationRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Short guestCount;
    private Long roomId;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
}