package com.reservio.reservation_system.presentation.controller;

import com.reservio.reservation_system.domain.service.RoomService;
import com.reservio.reservation_system.presentation.dto.reservation.RoomReservationDto;
import com.reservio.reservation_system.presentation.dto.room.RoomDetailsDto;
import com.reservio.reservation_system.presentation.dto.room.AvailableRoomDto;
import com.reservio.reservation_system.presentation.dto.room.RoomDto;
import com.reservio.reservation_system.presentation.dto.room.UpdateRoomStatusDto;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("hd/rooms")
@AllArgsConstructor
public class RoomController {
    private final RoomService roomService;


    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRooms() {
        List<RoomDto> rooms = roomService.getAllRoomsWithDetails();

        return ResponseEntity.ok(rooms);
    }

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

    @GetMapping("/available")
    public ResponseEntity<List<AvailableRoomDto>> getRooms(
            @RequestParam Long roomTypeId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        List<AvailableRoomDto> availableRooms = roomService.getAvailableRooms(roomTypeId, from, to);
        return ResponseEntity.ok(availableRooms);
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<Void> updateRoomStatus(
            @PathVariable Long id,
            @RequestBody UpdateRoomStatusDto dto
    ) {
        roomService.updateRoomStatus(id, dto.getStatus());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/price")
    public ResponseEntity<Void> updateRoomPrice(
            @PathVariable Long id,
            @RequestBody BigDecimal newPrice
    ) {
        roomService.updateRoomPricePerNight(id, newPrice);
        return ResponseEntity.noContent().build();
    }
}