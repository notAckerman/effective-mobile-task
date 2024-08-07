package org.example.effectivemobiletask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletask.util.exception.AccessDeniedException;
import org.example.effectivemobiletask.model.dto.task.TaskRequest;
import org.example.effectivemobiletask.model.dto.task.TaskResponse;
import org.example.effectivemobiletask.model.dto.task.TaskStatusUpdateRequest;
import org.example.effectivemobiletask.model.entity.Assignee;
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

        return ResponseEntity.ok(CustomModelMapper.toDtoList(tasks, TaskResponse.class));
    }

    @GetMapping("/users/{username}/tasks/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable String username, @PathVariable Long id) {
        Task task = taskService.getTask(id, username);

        return ResponseEntity.ok(CustomModelMapper.toDto(TaskResponse.class, task));
    }

    @DeleteMapping("/users/{username}/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable String username, @PathVariable Long id) {
        User user = getUser();
        checkPermission(user, username);

        taskService.deleteTask(id, user);
    }

    @PutMapping("/users/{username}/tasks/{id}")
    public ResponseEntity<TaskResponse> putTask(@PathVariable String username, @PathVariable Long id, @Valid @RequestBody TaskRequest taskRequest) {
        User user = getUser();
        checkPermission(user, username);

        Task task = CustomModelMapper.toEntity(Task.class, taskRequest);
        task.setAssignee(getAssignee(taskRequest));

        task = taskService.updateTask(id, task, user);

        return ResponseEntity.ok(CustomModelMapper.toDto(TaskResponse.class, task));
    }

    @PostMapping("/users/{username}/tasks")
    public ResponseEntity<TaskResponse> postTask(@RequestBody @Valid TaskRequest taskRequest, @PathVariable String username) {
        User user = getUser();
        checkPermission(user, username);

        Task task = CustomModelMapper.toEntity(Task.class, taskRequest);
        task.setAssignee(getAssignee(taskRequest));

        task = taskService.createTask(task, user);

        return new ResponseEntity<>(CustomModelMapper.toDto(TaskResponse.class, task), HttpStatus.CREATED);
    }

    @GetMapping("/tasks/available")
    public ResponseEntity<List<TaskResponse>> getAvailableTasks() {
        List<Task> tasks = taskService.getAllAvailableTasks();

        return ResponseEntity.ok(CustomModelMapper.toDtoList(tasks, TaskResponse.class));
    }

    @PatchMapping("/tasks/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long id, @RequestBody TaskStatusUpdateRequest status) {
        Task task = taskService.updateStatus(id, getUser(), status.getStatus());

        return ResponseEntity.ok(CustomModelMapper.toDto(TaskResponse.class, task));
    }

    @PostMapping("/tasks/{id}/assign")
    public ResponseEntity<TaskResponse> assignTask(@PathVariable Long id) {
        Task task = taskService.assignTask(id, getUser());

        return ResponseEntity.ok(CustomModelMapper.toDto(TaskResponse.class, task));
    }

    @GetMapping("/tasks/assigned")
    public ResponseEntity<List<TaskResponse>> getAssignedTasks() {
        List<Task> tasks = taskService.getAllAssignedTasks(getUser());

        return ResponseEntity.ok(CustomModelMapper.toDtoList(tasks, TaskResponse.class));
    }

    private void checkPermission(User user, String username) {
        if (!Objects.equals(user.getName(), username)) {
            throw new AccessDeniedException("You do not have permission to perform this action on this task");
        }
    }

    private Assignee getAssignee(TaskRequest taskRequest) {
        return taskRequest.getAssignee() == null ? null : assigneeService.getAssignee(taskRequest.getAssignee());
    }

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
