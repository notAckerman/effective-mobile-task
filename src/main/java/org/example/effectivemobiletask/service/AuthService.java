package org.example.effectivemobiletask.service;

import org.example.effectivemobiletask.model.dto.auth.AuthRequest;
import org.example.effectivemobiletask.model.dto.auth.AuthResponse;
import org.example.effectivemobiletask.model.dto.auth.RegistrationRequest;

public interface AuthService {

    AuthResponse register(RegistrationRequest request);

    AuthResponse authenticate(AuthRequest request);
}
