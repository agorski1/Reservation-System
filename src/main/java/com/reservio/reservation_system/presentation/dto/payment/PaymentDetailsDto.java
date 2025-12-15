package com.reservio.reservation_system.presentation.dto.payment;

import java.util.List;

public record PaymentDetailsDto(
        PaymentResponseDto summary,
        List<PaymentEntryDto> entries
) {}
