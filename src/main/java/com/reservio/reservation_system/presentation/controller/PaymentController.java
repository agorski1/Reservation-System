package com.reservio.reservation_system.presentation.controller;

import com.reservio.reservation_system.domain.service.PaymentService;
import com.reservio.reservation_system.presentation.dto.payment.PaymentRequestDto;
import com.reservio.reservation_system.presentation.dto.payment.PaymentResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@AllArgsConstructor
@RestController
@RequestMapping("hd/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResponseDto> processPayment(@RequestBody PaymentRequestDto dto,
                                               Principal principal) {
        String email = principal.getName();

        PaymentResponseDto response = paymentService.processPayment(dto.reservationId(),
                email,
                dto.amount(),
                dto.paymentMethod());


        return ResponseEntity.ok(response);
    }
}
