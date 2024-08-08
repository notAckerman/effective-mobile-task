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
import org.example.effectivemobiletask.model.dto.task.CommentRequest;
import org.example.effectivemobiletask.model.dto.task.CommentResponse;
import org.example.effectivemobiletask.model.entity.Comment;
import org.example.effectivemobiletask.model.entity.User;
import org.example.effectivemobiletask.service.CommentService;
import org.example.effectivemobiletask.util.CustomModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{username}/tasks/{task-id}/comments")
@RequiredArgsConstructor
@Tag(name = "Комментарии", description = "API для работы с комментариями к задачам")
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "Получить комментарии к задаче",
            description = "Возвращает список всех комментариев, связанных с указанной задачей.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение списка комментариев.",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentResponse.class)))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Доступ запрещен. Токен отсутствует или просрочен.",
                            content = @Content(schema = @Schema())
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Задача или комментарии не найдены.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @Parameter(description = "Идентификатор задачи") @PathVariable("task-id") Long taskId,
            @Parameter(description = "Имя пользователя") @PathVariable String username) {
        List<Comment> comments = commentService.getComments(taskId);

        return ResponseEntity.ok(CustomModelMapper.toDtoList(comments, CommentResponse.class));
    }

    @Operation(
            summary = "Создать новый комментарий",
            description = "Создает новый комментарий для указанной задачи.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Комментарий успешно создан.",
                            content = @Content(schema = @Schema(implementation = CommentResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные запроса.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Доступ запрещен. Токен отсутствует или просрочен.",
                            content = @Content(schema = @Schema())
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Задача не найдена.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @Parameter(description = "Имя пользователя") @PathVariable("username") String username,
            @Parameter(description = "Идентификатор задачи") @PathVariable("task-id") Long taskId,
            @Valid @RequestBody CommentRequest commentRequest) {
        Comment comment = CustomModelMapper.toEntity(Comment.class, commentRequest);
        comment = commentService.createComment(taskId, getUser(), comment);

        return new ResponseEntity<>(CustomModelMapper.toDto(CommentResponse.class, comment), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Обновить комментарий",
            description = "Обновляет существующий комментарий по его идентификатору.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Комментарий успешно обновлен.",
                            content = @Content(schema = @Schema(implementation = CommentResponse.class))
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
                            description = "Задача или комментарий не найдены.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }
    )
    @PutMapping("/{comment-id}")
    public ResponseEntity<CommentResponse> updateComment(
            @Parameter(description = "Имя пользователя") @PathVariable("username") String username,
            @Parameter(description = "Идентификатор задачи") @PathVariable("task-id") Long taskId,
            @Parameter(description = "Идентификатор комментария") @PathVariable("comment-id") Long commentId,
            @Valid @RequestBody CommentRequest commentRequest) {
        Comment comment = CustomModelMapper.toEntity(Comment.class, commentRequest);
        comment = commentService.updateComment(taskId, getUser(), commentId, comment);

        return ResponseEntity.ok(CustomModelMapper.toDto(CommentResponse.class, comment));
    }

    @Operation(
            summary = "Удалить комментарий",
            description = "Удаляет комментарий к задаче.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Комментарий успешно удален."
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Доступ запрещен. Токен отсутствует или просрочен.",
                            content = @Content(schema = @Schema())
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Комментарий или задача не найдены.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }
    )
    @DeleteMapping("/{comment-id}")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "Имя пользователя") @PathVariable("username") String username,
            @Parameter(description = "Идентификатор задачи") @PathVariable("task-id") Long taskId,
            @Parameter(description = "Идентификатор комментария") @PathVariable("comment-id") Long commentId) {
        commentService.deleteComment(taskId, getUser(), commentId);

        return ResponseEntity.noContent().build();
    }

    private static User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
