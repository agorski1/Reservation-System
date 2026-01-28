package com.reservio.reservation_system.infrastructure.user;

import com.reservio.reservation_system.domain.exception.EmailAlreadyExistsException;
import com.reservio.reservation_system.domain.repository.UserDao;
import com.reservio.reservation_system.domain.repository.UserRoleDao;
import com.reservio.reservation_system.infrastructure.entity.UserEntity;
import com.reservio.reservation_system.infrastructure.entity.UserRoleEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
public class RegistrationManager {
    private final UserDao userDao;
    private final UserRoleDao userRoleDao;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long registerUser(
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            String rawPassword
    ) {
        if (userDao.existsByUsrEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if (!isEmailValid(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);

        UserEntity user = new UserEntity();
        user.setUsrFirstName(firstName);
        user.setUsrLastName(lastName);
        user.setUsrEmail(email);
        user.setUsrPhoneNumber(phoneNumber);
        user.setUsrPassword(encodedPassword);
        user.setUsrRegistrationDate(LocalDate.now());

        UserRoleEntity userRole = userRoleDao.findByUrName("Customer")
                .orElseThrow(() -> new IllegalArgumentException("User role 'Customer' not found"));

        user.setUr(userRole);

        userDao.save(user);

        return user.getId();
    }

    private boolean isEmailValid(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}
