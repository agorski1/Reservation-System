package com.reservio.reservation_system.presentation.controller;

import com.reservio.reservation_system.domain.service.RoomTypeService;
import com.reservio.reservation_system.presentation.dto.room.AvailableRoomTypeDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("hd/room-type")
@AllArgsConstructor
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @GetMapping("/available")
    public ResponseEntity<List<AvailableRoomTypeDto>> getAvailableRoomTypes(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) List<String> amenities
    ) {
        return ResponseEntity.ok(roomTypeService.findAvailableRoomTypes(from, to, capacity, amenities));
    }

}
