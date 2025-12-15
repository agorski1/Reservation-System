package com.reservio.reservation_system.domain.service;

import com.reservio.reservation_system.domain.repository.UserRoleDao;
import com.reservio.reservation_system.presentation.dto.user.RoleDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserRoleService {
    private final UserRoleDao userRoleDao;

    public List<RoleDto> getAllRoles() {
        return userRoleDao.findAll()
                .stream()
                .map(role -> new RoleDto(
                        role.getId(),
                        role.getUrName()
                ))
                .toList();
    }
}
