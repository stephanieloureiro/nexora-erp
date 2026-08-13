package com.nexora.erp.auth.security;

import com.nexora.erp.auth.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DefaultUserSeeder {

    @Bean
    public ApplicationRunner seedDefaultUsers(UserRepository userRepository,
                                              JdbcTemplate jdbcTemplate,
                                              PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            String encodedPassword = passwordEncoder.encode("nexora123");

            jdbcTemplate.update("""
                    insert into users (name, email, password, role, active, created_at)
                    values (?, ?, ?, ?, true, current_timestamp)
                    """, "Administrador Nexora", "admin@nexora.com", encodedPassword, "ADMIN");

            jdbcTemplate.update("""
                    insert into users (name, email, password, role, active, created_at)
                    values (?, ?, ?, ?, true, current_timestamp)
                    """, "Operador Nexora", "operador@nexora.com", encodedPassword, "EMPLOYEE");
        };
    }
}
