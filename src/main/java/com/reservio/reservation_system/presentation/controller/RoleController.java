package com.reservio.reservation_system.presentation.controller;

import com.reservio.reservation_system.domain.service.UserRoleService;
import com.reservio.reservation_system.presentation.dto.user.RoleDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("hd/roles")
@AllArgsConstructor
public class RoleController {
    UserRoleService userRoleService;

    @GetMapping
    public ResponseEntity<List<RoleDto>> getRoles() {
        return ResponseEntity.ok(userRoleService.getAllRoles());
    }
}
