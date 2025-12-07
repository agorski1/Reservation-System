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

    @GetMapping("/{id}")
    public ResponseEntity<UserReservationDto> getReservationById(
            @PathVariable Long id,
            Principal principal) {

        String userEmail = principal.getName();

        UserReservationDto dto = reservationService.getReservationByIdForUser(id, userEmail);

        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{reservationId}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId, Principal principal) {
        String usrEmail = principal.getName();
        reservationService.cancelReservation(usrEmail, reservationId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reservationId}/status")
    public ResponseEntity<Void> updateReservationStatus(
            @PathVariable Long reservationId,
            @RequestBody UpdateStatusRequestDto request) {

        reservationService.updateReservationStatus(reservationId, request.getStatus());
        return ResponseEntity.ok().build();
    }

    @PostMapping()
    public ResponseEntity<RoomReservationResponseDto> reserveRoom(@Valid @RequestBody RoomReservationRequestDto dto, Principal principal) {

        String usrEmail = principal.getName();

        RoomReservationResponseDto response = reservationService.makeReservationForUser(dto.getRoomId(), usrEmail, dto.getGuestCount(), dto.getFrom(), dto.getTo());

        return ResponseEntity.ok(response);
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
