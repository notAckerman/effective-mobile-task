package org.example.effectivemobiletask.model.dto.task;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @NotEmpty(message = "Text cannot be empty")
    @Size(max = 1000, message = "Text can be up to 1000 characters long")
    private String text;
}
