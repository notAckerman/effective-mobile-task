package org.example.effectivemobiletask.model.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.effectivemobiletask.model.map.TaskPriority;
import org.example.effectivemobiletask.model.map.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private UserResponse author;
    private AssigneeResponse assignee;
    private LocalDateTime creationDate;
    private LocalDateTime dueDate;
    private LocalDateTime completionDate;
    private List<CommentResponse> comments;
}
