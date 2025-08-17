package com.reservio.reservation_system.presentation.dto.register;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterRequestDto {
    private String email;
    private String password;
}
