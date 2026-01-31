package com.reservio.reservation_system.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity public class SecurityConfig { @Bean public BCryptPasswordEncoder bCryptPasswordEncoder() { return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/hd/auth/register", "/hd/auth/login", "/hd/room-type/**")
                        .permitAll()

                        .requestMatchers(HttpMethod.POST, "/hd/reservations")
                        .hasRole("Customer")

                        .requestMatchers(HttpMethod.PATCH, "/hd/reservations/*/cancel")
                        .hasRole("Customer")

                        .requestMatchers(HttpMethod.POST, "/hd/payments/process")
                        .hasRole("Customer")

                        .requestMatchers("/hd/reservations/my/current", "/hd/rooms/available")
                        .hasAnyRole("Customer", "Employee")

                        .requestMatchers(HttpMethod.GET, "/hd/reservations/*").hasAnyRole("Customer", "Admin")

                        .requestMatchers(HttpMethod.GET, "/hd/dashboard/today").hasAnyRole("Employee", "Admin")

                        .requestMatchers(HttpMethod.GET, "/hd/rooms").hasAnyRole("Employee", "Admin")

                        .requestMatchers(HttpMethod.POST, "/hd/rooms/*/status").hasAnyRole("Employee", "Admin")

                        .requestMatchers(HttpMethod.POST, "/hd/room-type/price").hasRole("Admin")

                        .requestMatchers(HttpMethod.POST, "/hd/users/update").hasRole("Admin")

                        .requestMatchers(HttpMethod.POST, "/hd/users/employees").hasRole("Admin")

                        .requestMatchers(HttpMethod.POST, "/hd/users/password/change").hasRole("Admin")

                        .requestMatchers(HttpMethod.POST, "/hd/users/create/employee").hasRole("Admin")

                        .requestMatchers(HttpMethod.GET, "/hd/reservations").hasAnyRole("Employee", "Admin")

                        .requestMatchers(HttpMethod.GET, "/hd/payments/*").hasAnyRole("Employee", "Admin")

                        .requestMatchers(HttpMethod.GET, "/hd/reservations/employee/*").hasAnyRole("Employee", "Admin")

                        .requestMatchers(HttpMethod.POST, "/hd/reservations/manual").hasAnyRole("Employee", "Admin")

                        .requestMatchers(HttpMethod.GET, "/hd/payments/").hasAnyRole("Employee", "Admin")

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
