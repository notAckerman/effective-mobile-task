package org.example.effectivemobiletask.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletask.model.dto.exception.ExceptionResponse;
import org.example.effectivemobiletask.model.dto.task.TaskRequest;
import org.example.effectivemobiletask.model.dto.task.TaskResponse;
import org.example.effectivemobiletask.model.dto.task.TaskStatusUpdateRequest;
import org.example.effectivemobiletask.model.entity.Assignee;
import org.example.effectivemobiletask.model.entity.Task;
import org.example.effectivemobiletask.model.entity.User;
import org.example.effectivemobiletask.service.AssigneeService;
import org.example.effectivemobiletask.service.TaskService;
import org.example.effectivemobiletask.util.CustomModelMapper;
import org.example.effectivemobiletask.util.exception.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Задачи", description = "API для работы с задачами")
public class TaskController {

    private final TaskService taskService;

    private final AssigneeService assigneeService;

    @Operation(
            summary = "Получить список задач пользователя",
            description = "Возвращает список всех задач, связанных с указанным пользователем.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список задач успешно получен.",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskResponse.class)))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Доступ запрещен. Токен отсутствует или просрочен.",
                            content = @Content(schema = @Schema())
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }
    )
    @GetMapping("/users/{username}/tasks")
    public ResponseEntity<List<TaskResponse>> getTasks(
            @Parameter(description = "Имя пользователя") @PathVariable String username) {
        List<Task> tasks = taskService.getAllTasks(username);

        return ResponseEntity.ok(CustomModelMapper.toDtoList(tasks, TaskResponse.class));
    }

    @Operation(
            summary = "Получить задачу по ID",
            description = "Возвращает задачу по указанному ID и имени пользователя.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Задача успешно получена.",
                            content = @Content(schema = @Schema(implementation = TaskResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Доступ запрещен. Токен отсутствует или просрочен.",
                            content = @Content(schema = @Schema())
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Задача или пользователь не найдены.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }
    )
    @GetMapping("/users/{username}/tasks/{id}")
    public ResponseEntity<TaskResponse> getTask(
            @Parameter(description = "Имя пользователя") @PathVariable String username,
            @Parameter(description = "Идентификатор задачи") @PathVariable Long id) {
        Task task = taskService.getTask(id, username);

        return ResponseEntity.ok(CustomModelMapper.toDto(TaskResponse.class, task));
    }

    @Operation(
            summary = "Удалить задачу",
            description = "Удаляет задачу по указанному ID и имени пользователя.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Задача успешно удалена."
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Доступ запрещен. Токен отсутствует или просрочен.",
                            content = @Content(schema = @Schema())
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет прав для удаления задачи.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Задача или пользователь не найдены.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }
    )
    @DeleteMapping("/users/{username}/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "Имя пользователя") @PathVariable String username,
            @Parameter(description = "Идентификатор задачи") @PathVariable Long id) {
        User user = getUser();
        checkPermission(user, username);

        taskService.deleteTask(id, user);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Обновить задачу",
            description = "Обновляет задачу по указанному ID.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Задача успешно обновлена.",
                            content = @Content(schema = @Schema(implementation = TaskResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные запроса.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Доступ запрещен. Токен отсутствует или просрочен.",
                            content = @Content(schema = @Schema())
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет прав для обновления задачи.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Задача или пользователь не найдены.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }
    )
    @PutMapping("/users/{username}/tasks/{id}")
    public ResponseEntity<TaskResponse> putTask(
            @Parameter(description = "Имя пользователя") @PathVariable String username,
            @Parameter(description = "Идентификатор задачи") @PathVariable Long id,
            @Valid @RequestBody TaskRequest taskRequest) {
        User user = getUser();
        checkPermission(user, username);

        Task task = CustomModelMapper.toEntity(Task.class, taskRequest);
        task.setAssignee(getAssignee(taskRequest));

        task = taskService.updateTask(id, task, user);

        return ResponseEntity.ok(CustomModelMapper.toDto(TaskResponse.class, task));
    }

    @Operation(
            summary = "Создать новую задачу",
            description = "Создает новую задачу для указанного пользователя.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Задача успешно создана.",
                            content = @Content(schema = @Schema(implementation = TaskResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные запроса.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Доступ запрещен. Токен отсутствует или просрочен.",
                            content = @Content(schema = @Schema())
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет прав для создания задачи.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }
    )
    @PostMapping("/users/{username}/tasks")
    public ResponseEntity<TaskResponse> postTask(
            @Parameter(description = "Имя пользователя") @PathVariable String username,
            @RequestBody @Valid TaskRequest taskRequest) {
        User user = getUser();
        checkPermission(user, username);

        Task task = CustomModelMapper.toEntity(Task.class, taskRequest);
        task.setAssignee(getAssignee(taskRequest));

        task = taskService.createTask(task, user);

        return new ResponseEntity<>(CustomModelMapper.toDto(TaskResponse.class, task), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Получить доступные задачи",
            description = "Возвращает список всех доступных задач.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список доступных задач успешно получен.",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskResponse.class)))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Доступ запрещен. Токен отсутствует или просрочен.",
                            content = @Content(schema = @Schema())
                    )
            }
    )
    @GetMapping("/tasks/available")
    public ResponseEntity<List<TaskResponse>> getAvailableTasks() {
        List<Task> tasks = taskService.getAllAvailableTasks();

        return ResponseEntity.ok(CustomModelMapper.toDtoList(tasks, TaskResponse.class));
    }

    @Operation(
            summary = "Обновить статус задачи",
            description = "Обновляет статус задачи по указанному ID.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Статус задачи успешно обновлен.",
                            content = @Content(schema = @Schema(implementation = TaskResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные запроса.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Доступ запрещен. Токен отсутствует или просрочен.",
                            content = @Content(schema = @Schema())
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Задача не найдена.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }
    )
    @PatchMapping("/tasks/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @Parameter(description = "Идентификатор задачи") @PathVariable Long id,
            @RequestBody TaskStatusUpdateRequest status) {
        Task task = taskService.updateStatus(id, getUser(), status.getStatus());

        return ResponseEntity.ok(CustomModelMapper.toDto(TaskResponse.class, task));
    }

    @Operation(
            summary = "Назначить задачу",
            description = "Назначает задачу текущему пользователю по указанному ID.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Задача успешно назначена.",
                            content = @Content(schema = @Schema(implementation = TaskResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Доступ запрещен. Токен отсутствует или просрочен.",
                            content = @Content(schema = @Schema())
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Задача не найдена.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }
    )
    @PostMapping("/tasks/{id}/assign")
    public ResponseEntity<TaskResponse> assignTask(
            @Parameter(description = "Идентификатор задачи") @PathVariable Long id) {
        Task task = taskService.assignTask(id, getUser());

        return ResponseEntity.ok(CustomModelMapper.toDto(TaskResponse.class, task));
    }

    @Operation(
            summary = "Получить назначенные задачи",
            description = "Возвращает список всех задач, назначенных текущему пользователю.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список назначенных задач успешно получен.",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskResponse.class)))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Доступ запрещен. Токен отсутствует или просрочен.",
                            content = @Content(schema = @Schema())
                    )
            }
    )
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
