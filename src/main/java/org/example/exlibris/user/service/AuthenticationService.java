package org.example.exlibris.user.service;

import org.example.exlibris.security.service.JwtService;
import org.example.exlibris.user.role.Role;
import org.example.exlibris.user.entity.User;
import org.example.exlibris.user.repository.UserRepository;
import org.example.exlibris.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
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

    public RegisterResponse register(RegisterRequest request) {
        if (repo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User already exists");
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
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails user = org.springframework.security.core.userdetails.User
                .withUsername(request.getEmail())
                .password("N/A")
                .roles("USER")
                .build();

        return LoginResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }
}
