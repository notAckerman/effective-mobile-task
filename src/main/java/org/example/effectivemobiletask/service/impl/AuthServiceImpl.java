package org.example.effectivemobiletask.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletask.model.dto.auth.AuthRequest;
import org.example.effectivemobiletask.model.dto.auth.AuthResponse;
import org.example.effectivemobiletask.model.dto.auth.RegistrationRequest;
import org.example.effectivemobiletask.model.entity.User;
import org.example.effectivemobiletask.model.map.UserRole;
import org.example.effectivemobiletask.repository.UserRepository;
import org.example.effectivemobiletask.security.jwt.JwtService;
import org.example.effectivemobiletask.service.AuthService;
import org.example.effectivemobiletask.util.exception.NotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegistrationRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(UserRole.ROLE_USER)
                .tasks(new ArrayList<>())
                .build();

        userRepository.save(user);

        return new AuthResponse(jwtService.generateToken(user));
    }

    @Override
    @Transactional
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        return new AuthResponse(jwtService.generateToken(user));
    }
}
