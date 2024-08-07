package org.example.effectivemobiletask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletask.model.dto.auth.AuthRequest;
import org.example.effectivemobiletask.model.dto.auth.AuthResponse;
import org.example.effectivemobiletask.model.dto.auth.RegistrationRequest;
import org.example.effectivemobiletask.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody @Valid RegistrationRequest request
    ) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody @Valid AuthRequest request
    ) {
        AuthResponse response = authService.authenticate(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
