package com.reservio.reservation_system.presentation.controller;

import com.reservio.reservation_system.infrastructure.entity.ReservationStatusEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("hd/payments")
public class PaymentController {
    @PostMapping()
    public ResponseEntity<Void> getRooms(@RequestBody List<ReservationStatusEntity> reservationStatusEntities) {


        return ResponseEntity.ok().build();
    }
}
