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

    @PostMapping("/password/change")
    public ResponseEntity<String> changeUserPasswordByAdmin(@RequestBody AdminPasswordChangeDto dto) {
        userService.changeUserPasswordByAdmin(dto.getUserId(), dto.getNewPassword());
        return ResponseEntity.ok("Password changed successfully by admin");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateUserByAdmin(@RequestBody EmployeeDto updateDto) {

        if (updateDto.getId() == null) {
            return ResponseEntity.badRequest().body("User ID is required");
        }

        userService.updateUserByAdmin(updateDto.getId(), updateDto);
        return ResponseEntity.ok("User updated successfully");
    }

    @PostMapping("/create/employee")
    public ResponseEntity<String> createUserByAdmin(@RequestBody EmployeeDto createDto) {
        if (createDto.getEmail() == null || createDto.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Email is required for new user");
        }

        userService.createUserByAdmin(createDto);
        return ResponseEntity.ok("User created successfully");
    }


}