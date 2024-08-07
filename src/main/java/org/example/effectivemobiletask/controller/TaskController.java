package org.example.effectivemobiletask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletask.exception.AccessDeniedException;
import org.example.effectivemobiletask.model.dto.task.TaskRequest;
import org.example.effectivemobiletask.model.dto.task.TaskResponse;
import org.example.effectivemobiletask.model.dto.task.TaskStatusUpdateRequest;
import org.example.effectivemobiletask.model.entity.Task;
import org.example.effectivemobiletask.model.entity.User;
import org.example.effectivemobiletask.service.AssigneeService;
import org.example.effectivemobiletask.service.TaskService;
import org.example.effectivemobiletask.util.CustomModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final AssigneeService assigneeService;

    @GetMapping("/users/{username}/tasks")
    public ResponseEntity<List<TaskResponse>> getTasks(@PathVariable String username) {
        List<Task> tasks = taskService.getAllTasks(username);
        List<TaskResponse> response = CustomModelMapper.toDtoList(tasks, TaskResponse.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{username}/tasks/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable String username, @PathVariable long id) {
        Task task = taskService.getTask(id, username);
        TaskResponse response = CustomModelMapper.toDto(TaskResponse.class, task);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{username}/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable String username, @PathVariable long id) {
        User user = getUser();
        if (!Objects.equals(user.getName(), username)) {
            throw new AccessDeniedException("You do not have permission to delete this task");
        }
        taskService.deleteTask(id, user);
    }

    @PutMapping("/users/{username}/tasks/{id}")
    public ResponseEntity<TaskResponse> putTask(@PathVariable String username, @PathVariable long id, @Valid @RequestBody TaskRequest taskRequest) {
        User user = getUser();
        if (!Objects.equals(user.getName(), username)) {
            throw new AccessDeniedException("You do not have permission to update this task");
        }
        Task task = CustomModelMapper.toEntity(Task.class, taskRequest);
        task.setAssignee(taskRequest.getAssignee() == null ? null : assigneeService.getAssignee(taskRequest.getAssignee()));
        task = taskService.updateTask(id, task, user);
        TaskResponse response = CustomModelMapper.toDto(TaskResponse.class, task);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tasks/available")
    public ResponseEntity<List<TaskResponse>> getAvailableTasks() {
        List<Task> tasks = taskService.getAllAvailableTasks();
        List<TaskResponse> response = CustomModelMapper.toDtoList(tasks, TaskResponse.class);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/tasks/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable long id, @RequestBody TaskStatusUpdateRequest status) {
        User user = getUser();
        Task task = taskService.updateStatus(id, user, status.getStatus());
        TaskResponse response = CustomModelMapper.toDto(TaskResponse.class, task);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tasks/{id}/assign")
    public ResponseEntity<TaskResponse> assignTask(@PathVariable long id) {
        User user = getUser();
        Task task = taskService.assignTask(id, user);
        TaskResponse response = CustomModelMapper.toDto(TaskResponse.class, task);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tasks/assigned")
    public ResponseEntity<List<TaskResponse>> getAssignedTasks() {
        User user = getUser();
        List<Task> tasks = taskService.getAllAssignedTasks(user);
        List<TaskResponse> response = CustomModelMapper.toDtoList(tasks, TaskResponse.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskResponse> postTask(@RequestBody @Valid TaskRequest taskRequest) {
        User user = getUser();
        Task task = CustomModelMapper.toEntity(Task.class, taskRequest);
        task.setAssignee(taskRequest.getAssignee() == null ? null : assigneeService.getAssignee(taskRequest.getAssignee()));
        task = taskService.createTask(task, user);
        TaskResponse response = CustomModelMapper.toDto(TaskResponse.class, task);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private static User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
