package org.example.effectivemobiletask.model.dto.task;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.effectivemobiletask.model.entity.Assignee;
import org.example.effectivemobiletask.model.map.TaskPriority;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    @NotEmpty(message = "Title cannot be empty")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotEmpty(message = "Description cannot be empty")
    @Size(max = 1000, message = "Description can be up to 1000 characters long")
    private String description;

    @NotNull(message = "Priority cannot be null")
    private TaskPriority priority;

    @NotNull(message = "Due date cannot be null")
    @FutureOrPresent(message = "Due date must be in the present or future")
    private LocalDateTime dueDate;

    private Long assignee;
}
