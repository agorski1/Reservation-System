package com.reservio.reservation_system.presentation.controller;

import com.reservio.reservation_system.domain.service.ReservationService;
import com.reservio.reservation_system.presentation.dto.reservation.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

    @GetMapping("/my/current")
    public ResponseEntity<List<UserReservationDto>> getMyCurrentReservations(Principal principal) {
        String usrEmail = principal.getName();
        List<UserReservationDto> userReservations = reservationService.getCurrentUserReservations(usrEmail);

        return ResponseEntity.ok(userReservations);
    }

    @GetMapping("/my")
    public ResponseEntity<List<UserReservationDto>> getMyReservations(Principal principal) {
        String usrEmail = principal.getName();
        List<UserReservationDto> userReservations = reservationService.getAllUserReservations(usrEmail);

        return ResponseEntity.ok(userReservations);
    }

    @PatchMapping("/{reservationId}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId, Principal principal) {
        String usrEmail = principal.getName();
        reservationService.cancelReservation(usrEmail, reservationId);

        return ResponseEntity.ok().build();
    }

    @PostMapping()
    public ResponseEntity<Void> reserveRoom(@Valid @RequestBody RoomReservationRequestDto dto, Principal principal) {
        String usrEmail = principal.getName();
        RoomReservationResponseDto roomReservationResponseDto = reservationService.makeReservationForUser(dto.getRoomId(),
                usrEmail,
                dto.getGuestCount(),
                dto.getFrom(),
                dto.getTo());

        return ResponseEntity.ok().build();
    }


    @GetMapping()
    public ResponseEntity<List<ReservationDto>> getReservations(@RequestParam(required = false, defaultValue = "false") boolean all, @RequestParam(required = false) LocalDateTime from, @RequestParam(required = false) LocalDateTime to, @RequestParam(required = false) String phone, @RequestParam(required = false) String email) {
        List<ReservationDto> reservations = reservationService.getFilteredReservations(all, from, to, phone, email);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/manual")
    public ResponseEntity<Void> createManualReservation(@RequestBody ManualReservationRequestDto dto) {
        reservationService.createManualReservation(dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getPhoneNumber(), dto.getGuestCount(), dto.getRoomId(), dto.getCheckInDate(), dto.getCheckOutDate());
        return ResponseEntity.ok().build();
    }

}
