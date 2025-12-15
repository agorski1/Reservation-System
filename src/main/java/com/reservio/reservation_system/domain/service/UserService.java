package com.reservio.reservation_system.domain.service;

import com.reservio.reservation_system.domain.model.User;
import com.reservio.reservation_system.domain.repository.UserDao;
import com.reservio.reservation_system.domain.repository.UserRoleDao;
import com.reservio.reservation_system.infrastructure.entity.UserEntity;
import com.reservio.reservation_system.infrastructure.entity.UserRoleEntity;
import com.reservio.reservation_system.presentation.dto.user.EmployeeDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserDao userDao;
    private final UserRoleDao userRoleDao;
    private final PasswordEncoder passwordEncoder;

    public List<EmployeeDto> getAllEmployees() {
        return userDao.findAllEmployees().stream()
                .map(u -> new EmployeeDto(
                        u.getId(),
                        u.getUsrFirstName(),
                        u.getUsrLastName(),
                        u.getUsrEmail(),
                        u.getUsrPhoneNumber(),
                        u.getUsrCity(),
                        u.getUsrZipCode(),
                        u.getUsrStreet()
                ))
                .toList();
    }

    @Transactional
    public void changeUserPasswordByAdmin(long userId, String newPassword) {
        UserEntity user = userDao.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.setUsrPassword(passwordEncoder.encode(newPassword));
        userDao.save(user);
    }

    public void updateUserByAdmin(Long userId, EmployeeDto updateDto) {
        UserEntity user = userDao.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (updateDto.getFirstName() != null) {
            user.setUsrFirstName(updateDto.getFirstName());
        }
        if (updateDto.getLastName() != null) {
            user.setUsrLastName(updateDto.getLastName());
        }
        if (updateDto.getEmail() != null) {
            user.setUsrEmail(updateDto.getEmail());
        }
        if (updateDto.getPhoneNumber() != null) {
            user.setUsrPhoneNumber(updateDto.getPhoneNumber());
        }
        if (updateDto.getCity() != null) {
            user.setUsrCity(updateDto.getCity());
        }
        if (updateDto.getZipCode() != null) {
            user.setUsrZipCode(updateDto.getZipCode());
        }
        if (updateDto.getStreet() != null) {
            user.setUsrStreet(updateDto.getStreet());
        }

        userDao.save(user);
    }

    @Transactional
    public void createUserByAdmin(EmployeeDto createDto) {
        if (createDto.getFirstName() == null || createDto.getFirstName().trim().isBlank()) {
            throw new IllegalArgumentException("Imię jest wymagane");
        }
        if (createDto.getLastName() == null || createDto.getLastName().trim().isBlank()) {
            throw new IllegalArgumentException("Nazwisko jest wymagane");
        }
        if (createDto.getEmail() == null || createDto.getEmail().trim().isBlank()) {
            throw new IllegalArgumentException("Email jest wymagany");
        }

        String email = createDto.getEmail().trim().toLowerCase();

        if (userDao.existsByUsrEmail(email)) {
            throw new IllegalArgumentException("Użytkownik z tym adresem email już istnieje");
        }

        UserRoleEntity employeeRole = userRoleDao.findByUrName("Employee")
                .orElseThrow(() -> new IllegalArgumentException("Rola 'Employee' nie istnieje w systemie"));

        UserEntity newUser = new UserEntity();
        newUser.setUsrFirstName(createDto.getFirstName().trim());
        newUser.setUsrLastName(createDto.getLastName().trim());
        newUser.setUsrEmail(email);
        newUser.setUsrPhoneNumber(createDto.getPhoneNumber());
        newUser.setUsrCity(createDto.getCity());
        newUser.setUsrZipCode(createDto.getZipCode());
        newUser.setUsrStreet(createDto.getStreet());

        String tempPassword = "tempPass123!";
        newUser.setUsrPassword(passwordEncoder.encode(tempPassword));

        newUser.setUr(employeeRole);

        userDao.save(newUser);
    }
}
