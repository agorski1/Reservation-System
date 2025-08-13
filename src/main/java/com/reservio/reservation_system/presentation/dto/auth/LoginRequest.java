package com.reservio.reservation_system.presentation.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Login cannot be empty")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    private String password;
}

