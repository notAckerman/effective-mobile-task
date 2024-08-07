package org.example.effectivemobiletask;

import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletask.model.entity.Assignee;
import org.example.effectivemobiletask.model.map.UserRole;
import org.example.effectivemobiletask.repository.AssigneeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@RequiredArgsConstructor
public class EffectiveMobileTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(EffectiveMobileTaskApplication.class, args);
    }
}
