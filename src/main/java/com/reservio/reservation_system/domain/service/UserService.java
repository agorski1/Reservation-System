package com.reservio.reservation_system.domain.service;

import com.reservio.reservation_system.domain.repository.UserDao;
import com.reservio.reservation_system.infrastructure.entity.UserEntity;
import com.reservio.reservation_system.presentation.dto.user.EmployeeDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public List<EmployeeDto> getAllEmployees() {
        return userDao.findAllEmployees().stream()
                .map(u -> new EmployeeDto(
                        u.getId(),
                        u.getUsrFirstName(),
                        u.getUsrLastName(),
                        u.getUsrEmail(),
                        u.getUsrPhoneNumber(),
                        u.getUsrCity()
                ))
                .toList();
    }

    @Transactional
    public void changeUserPasswordByAdmin(long  userId, String newPassword) {
        UserEntity user = userDao.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.setUsrPassword(passwordEncoder.encode(newPassword));
        userDao.save(user);
    }
}
