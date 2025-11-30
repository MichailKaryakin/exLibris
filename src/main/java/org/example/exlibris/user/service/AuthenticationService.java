package org.example.exlibris.user.service;

import lombok.RequiredArgsConstructor;
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
public class AuthenticationService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public RegisterResponse register(RegisterRequest request) {
        if (repo.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyUsedException();
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(Role.USER)
                .createdAt(java.time.LocalDateTime.now())
                .build();

        repo.save(user);

        return RegisterResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }

    public LoginResponse login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());

        return LoginResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

    public RefreshResponse refresh(String refreshToken) {
        String email = jwtService.extractUsername(refreshToken);
        UserDetails user = userDetailsService.loadUserByUsername(email);

        if (!jwtService.isRefreshTokenValid(refreshToken, user)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccess = jwtService.generateToken(user);

        return RefreshResponse.builder()
                .accessToken(newAccess)
                .build();
    }
}
