package com.nexora.erp.auth.service;

import com.nexora.erp.auth.dto.LoginRequest;
import com.nexora.erp.auth.dto.LoginResponse;
import com.nexora.erp.auth.security.AuthenticatedUser;
import com.nexora.erp.auth.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        return new LoginResponse("Bearer", token, jwtService.getExpirationSeconds());
    }
}
