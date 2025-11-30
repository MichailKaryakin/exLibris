package org.example.exlibris.user.controller;

import org.example.exlibris.user.service.AuthenticationService;
import org.example.exlibris.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return service.login(request);
    }

    @PostMapping("/refresh")
    public RefreshResponse login(@RequestBody RefreshRequest request) {
        return service.refresh(request.getRefreshToken());
    }
}
