package com.reservio.reservation_system.presentation.controller;

import com.reservio.reservation_system.domain.service.RoomTypeService;
import com.reservio.reservation_system.presentation.dto.room.AvailableRoomTypeDto;
import com.reservio.reservation_system.presentation.dto.room.RoomTypeDto;
import com.reservio.reservation_system.presentation.dto.room.UpdateRoomTypePriceDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("hd/room-type")
@AllArgsConstructor
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @GetMapping
    public ResponseEntity<List<RoomTypeDto>> getAllRoomTypes() {
        List<RoomTypeDto> roomTypes = roomTypeService.getRoomTypes();
        return ResponseEntity.ok(roomTypes);
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableRoomTypeDto>> getAvailableRoomTypes(
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @RequestParam(required = false) List<Integer> capacity,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) List<String> amenities
    ) {
        return ResponseEntity.ok(roomTypeService.findAvailableRoomTypes(from,
                to,
                capacity,
                minPrice,
                maxPrice,
                amenities));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeDto> getRoomTypeById(@PathVariable Long id) {
        RoomTypeDto roomType = roomTypeService.findRoomTypeById(id);
        return ResponseEntity.ok(roomType);
    }

    @PostMapping("/price")
    public ResponseEntity<Void> updateRoomPrice(
            @RequestBody UpdateRoomTypePriceDto dto
    ) {
        roomTypeService.updatePricePerNight(dto.getRoomTypeId(), dto.getPricePerNight());
        return ResponseEntity.noContent().build();
    }
}
