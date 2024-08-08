package org.example.effectivemobiletask.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletask.model.dto.auth.AuthRequest;
import org.example.effectivemobiletask.model.dto.auth.AuthResponse;
import org.example.effectivemobiletask.model.dto.auth.RegistrationRequest;
import org.example.effectivemobiletask.model.dto.exception.ExceptionResponse;
import org.example.effectivemobiletask.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "API для аутентификации и регистрации пользователей")
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создает нового пользователя и возвращает токен аутентификации.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Пользователь успешно зарегистрирован.",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные запроса.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegistrationRequest request) {
        AuthResponse response = authService.register(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Проверяет учетные данные пользователя и возвращает токен аутентификации.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Пользователь успешно аутентифицирован.",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные запроса.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Неверный логин или пароль.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }
    )
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid AuthRequest request) {
        AuthResponse response = authService.authenticate(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
