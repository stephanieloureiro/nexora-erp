package com.nexora.erp.auth.repository;

import com.nexora.erp.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCaseAndActiveTrue(String email);
}
