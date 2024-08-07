package org.example.effectivemobiletask.model.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.effectivemobiletask.validator.annotation.UniqueEmail;
import org.example.effectivemobiletask.validator.annotation.UniqueName;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "Name must not be empty")
    @Size(max = 64, message = "Name should not exceed 64 characters")
    @UniqueName
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email must not be empty")
    @Size(max = 256, message = "Email should not exceed 256 characters")
    @UniqueEmail
    private String email;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 8, max = 32, message = "Password should be between 8 and 32 characters")
    private String password;
}
