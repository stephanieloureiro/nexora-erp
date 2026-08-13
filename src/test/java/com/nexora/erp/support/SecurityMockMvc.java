package com.nexora.erp.support;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.Instant;
import java.util.List;

public final class SecurityMockMvc {

    private SecurityMockMvc() {
    }

    public static RequestPostProcessor admin() {
        return userWithRole("ROLE_ADMIN");
    }

    public static RequestPostProcessor employee() {
        return userWithRole("ROLE_EMPLOYEE");
    }

    private static RequestPostProcessor userWithRole(String role) {
        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "HS256")
                .issuer("nexora-erp")
                .subject("test@nexora.com")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .claim("scope", role)
                .build();

        return SecurityMockMvcRequestPostProcessors.jwt()
                .jwt(jwt)
                .authorities(List.of(new SimpleGrantedAuthority(role)));
    }
}
