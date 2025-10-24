package com.reservio.reservation_system.presentation.controller;

import com.reservio.reservation_system.domain.service.RoomService;
import com.reservio.reservation_system.presentation.dto.reservation.RoomReservationDto;
import com.reservio.reservation_system.presentation.dto.room.RoomDetailsDto;
import com.reservio.reservation_system.presentation.dto.room.RoomDto;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("hd/desks")
@AllArgsConstructor
public class RoomController {
    private final RoomService roomService;


//    @GetMapping
//    public ResponseEntity<List<RoomDto>> getAllRooms() {
//        List<RoomDto> rooms = roomService.getAllRoomsWithDetails();
//
//        return ResponseEntity.ok(rooms);
//    }

    @GetMapping("/{deskId}/current-reservations")
    public ResponseEntity<List<RoomReservationDto>> getCurrentReservations(@PathVariable Long deskId) {
        List<RoomReservationDto> currentReservations = roomService.getCurrentReservationsForRoom(deskId);

        return ResponseEntity.ok(currentReservations);
    }

    @GetMapping("/availability")
    public ResponseEntity<List<RoomDetailsDto>> getDesksAvailability(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        List<RoomDetailsDto> availability = roomService.getRoomsAvailability(from, to);
        return ResponseEntity.ok(availability);
    }

//    @GetMapping("/{deskId}/reservations")
//    public ResponseEntity<List<RoomReservationDto>> getDesksAvailability(
//            @PathVariable Long deskId,
//            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
//            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
//
//        List<RoomReservationDto> reservation = roomService.getRoomReservations(deskId, from, to);
//
//        return ResponseEntity.ok(reservation);
//    }
}