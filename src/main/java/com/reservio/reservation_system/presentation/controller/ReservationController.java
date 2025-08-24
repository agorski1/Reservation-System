package com.reservio.reservation_system.presentation.controller;

import com.reservio.reservation_system.domain.service.ReservationService;
import com.reservio.reservation_system.presentation.dto.reservation.DeskReservationRequestDto;
import com.reservio.reservation_system.presentation.dto.reservation.DeskReservationResponseDto;
import com.reservio.reservation_system.presentation.dto.reservation.UserReservationDto;
import com.reservio.reservation_system.presentation.dto.room.RoomDetailsDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("hd/reservations")
@AllArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/my")
    public ResponseEntity<List<UserReservationDto>> getMyReservations(Principal principal) {
        String usrEmail = principal.getName();
        List<UserReservationDto> userReservations = reservationService.getCurrentUserReservations(usrEmail);

        return ResponseEntity.ok(userReservations);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId, Principal principal) {
        String usrEmail = principal.getName();
        reservationService.cancelReservation(usrEmail, reservationId);

        return ResponseEntity.ok().build();
    }

    @PostMapping()
    public ResponseEntity<Void> reserveRoom(@Valid @RequestBody DeskReservationRequestDto dto, Principal principal) {
        String usrEmail = principal.getName();
        DeskReservationResponseDto deskReservationResponseDto = reservationService.reserveRoom(dto.getDeskId(), usrEmail, dto.getFrom(), dto.getTo());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Long deskId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        boolean available = reservationService.isDeskAvailable(deskId, from, to);
        return ResponseEntity.ok(available);
    }
}
