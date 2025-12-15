package com.reservio.reservation_system.presentation.dto.payment;

import java.time.LocalDateTime;

public record PaymentEntryDto(
        Float amount,
        LocalDateTime date,
        String method,
        String status
) {}