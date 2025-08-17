package com.reservio.reservation_system.presentation.controller;

import com.reservio.reservation_system.infrastructure.security.UserAuthenticator;
import com.reservio.reservation_system.infrastructure.user.RegistrationManager;
import com.reservio.reservation_system.presentation.dto.auth.LoginRequestDto;
import com.reservio.reservation_system.presentation.dto.auth.LoginResponseDto;
import com.reservio.reservation_system.presentation.dto.register.RegisterRequestDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rs/auth")
@AllArgsConstructor
public class AuthController {
    private final UserAuthenticator userAuthenticator;
    private final RegistrationManager  registrationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        String token = userAuthenticator.authenticate(dto.getEmail(), dto.getPassword());

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDto dto) {
        Long id = registrationManager.registerUser(dto.getEmail(), dto.getPassword());

        return ResponseEntity.ok().build();
    }

}
