package com.reservio.reservation_system.presentation.controller;

import com.reservio.reservation_system.domain.service.UserService;
import com.reservio.reservation_system.presentation.dto.user.AdminPasswordChangeDto;
import com.reservio.reservation_system.presentation.dto.user.EmployeeDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("hd/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = userService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PatchMapping("/{userId}/password")
    public ResponseEntity<String> changeUserPasswordByAdmin(
            @PathVariable Long userId,
            @RequestBody AdminPasswordChangeDto dto
    ) {
        userService.changeUserPasswordByAdmin(userId, dto.getNewPassword());
        return ResponseEntity.ok("Password changed successfully by admin");
    }
}