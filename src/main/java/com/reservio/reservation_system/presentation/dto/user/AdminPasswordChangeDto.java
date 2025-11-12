package com.reservio.reservation_system.presentation.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminPasswordChangeDto {
    private String newPassword;
}
