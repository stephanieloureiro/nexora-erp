package com.nexora.erp.auth.service;

import com.nexora.erp.auth.repository.UserRepository;
import com.nexora.erp.auth.security.AuthenticatedUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmailIgnoreCaseAndActiveTrue(username)
                .map(AuthenticatedUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario ou senha invalidos."));
    }
}
