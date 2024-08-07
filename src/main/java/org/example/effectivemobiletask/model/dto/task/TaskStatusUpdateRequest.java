package org.example.effectivemobiletask.model.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.effectivemobiletask.model.map.TaskStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusUpdateRequest {
    @NotNull(message = "Status cannot be null")
    private TaskStatus status;
}
