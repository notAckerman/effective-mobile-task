package org.example.effectivemobiletask.model.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @Size(max = 256, message = "Email should not exceed 256 characters")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 32, message = "Password should be between 8 and 32 characters")
    private String password;
}
