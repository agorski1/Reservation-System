package com.reservio.reservation_system.infrastructure.security;

import com.reservio.reservation_system.domain.repository.UserDao;
import com.reservio.reservation_system.infrastructure.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserAuthenticator {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public String authenticate(String email, String password) {
        UserEntity user = userDao.findByUsrEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(password, user.getUsrPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        String roleName = user.getUr().getUrName();


        return jwtUtils.generateToken(user.getUsrEmail(), roleName);
    }

}
