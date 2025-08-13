package com.reservio.reservation_system.infrastructure.user;

import com.reservio.reservation_system.domain.repository.UserDao;
import com.reservio.reservation_system.domain.repository.UserRoleDao;
import com.reservio.reservation_system.infrastructure.entity.UserEntity;
import com.reservio.reservation_system.infrastructure.entity.UserRoleEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.LinkOption;
import java.time.LocalDate;

@AllArgsConstructor
@Service
@Transactional
public class RegistrationManager {
    private final UserDao userDao;
    private final UserRoleDao userRoleDao;
    private final PasswordEncoder passwordEncoder;

    public Long registerUser(String email, String rawPassword) {
        if (userDao.existsByUsrEmail(email)) {
            throw new IllegalArgumentException("Email already exists!");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);

        UserEntity user = new UserEntity();
        user.setUsrEmail(email);
        user.setUsrPassword(encodedPassword);
        user.setUsrRegistrationDate(LocalDate.now());

        UserRoleEntity userRole = userRoleDao.findByUrName("Customer");
        if (userRole == null) {
            throw new IllegalStateException("Default role 'Customer' not found");
        }

        user.setUr(userRole);
        userDao.save(user);

        return user.getId();

    }
}
