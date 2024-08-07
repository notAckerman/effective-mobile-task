package org.example.effectivemobiletask.security;

import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletask.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authProvider;

    private final JwtAuthenticationFilter jwtAuthFilter;

    public String[] PERMIT_ALL = {
            "/swagger*/**",
            "/swagger-ui/**",
            "/backend/swagger-ui.html",
            "/documentation/**",
            "/v3/api-docs/**",
            "/api/v1/auth/**"
    };

    public String[] USER = {
            "/api/v1/users/**",
            "/api/v1/tasks",
            "/api/v1/assignees",
            "/api/v1/comments/**"
    };

    public String[] ASSIGNEE = {
            "/api/v1/users/**",
            "/api/v1/comments/**",
            "api/v1/tasks/**"
    };

    public String[] ADMIN = {

    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(USER).hasAnyRole("USER", "ASSIGNEE")
                        .requestMatchers(ASSIGNEE).hasRole("ASSIGNEE")
                        .requestMatchers(ADMIN).hasRole("ADMIN")
                        .requestMatchers(PERMIT_ALL).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();
    }
}
