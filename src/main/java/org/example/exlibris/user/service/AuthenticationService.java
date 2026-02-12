package org.example.exlibris.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.exlibris.security.exception.JwtAuthenticationException;
import org.example.exlibris.security.jwt.JwtService;
import org.example.exlibris.security.userdetails.CustomUserDetailsService;
import org.example.exlibris.user.dto.*;
import org.example.exlibris.user.entity.User;
import org.example.exlibris.user.enums.Role;
import org.example.exlibris.user.exception.EmailAlreadyUsedException;
import org.example.exlibris.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public RegisterResponse register(RegisterRequest request) {
        if (repo.existsByEmail(request.email())) {
            throw new EmailAlreadyUsedException();
        }

        User user = User.builder()
                .email(request.email())
                .password(encoder.encode(request.password()))
                .role(Role.USER)
                .build();

        repo.save(user);

        return new RegisterResponse(user.getId(), user.getEmail());
    }

    public LoginResponse login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetails user = userDetailsService.loadUserByUsername(request.email());

        return new LoginResponse(
                jwtService.generateToken(user),
                jwtService.generateRefreshToken(user)
        );
    }

    public LoginResponse refresh(String refreshToken) {
        String email = jwtService.extractUsername(refreshToken);
        UserDetails user = userDetailsService.loadUserByUsername(email);

        if (!jwtService.isRefreshTokenValid(refreshToken, user)) {
            throw new JwtAuthenticationException("Invalid or expired refresh token");
        }

        return new LoginResponse(
                jwtService.generateToken(user),
                jwtService.generateRefreshToken(user)
        );
    }
}
