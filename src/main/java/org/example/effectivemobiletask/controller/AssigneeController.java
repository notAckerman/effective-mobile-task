package org.example.effectivemobiletask.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletask.model.dto.task.AssigneeResponse;
import org.example.effectivemobiletask.model.entity.Assignee;
import org.example.effectivemobiletask.service.AssigneeService;
import org.example.effectivemobiletask.util.CustomModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assignees")
@RequiredArgsConstructor
@Tag(name = "Исполнители", description = "API для работы с исполнителями")
public class AssigneeController {

    private final AssigneeService assigneeService;

    @Operation(
            summary = "Получить список всех исполнителей",
            description = "Возвращает список всех исполнителей в системе.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение списка",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AssigneeResponse.class)))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Доступ запрещен. Токен отсутствует или просрочен.",
                            content = @Content(schema = @Schema())
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<AssigneeResponse>> getAllAssignees() {
        List<Assignee> assignees = assigneeService.getAssignees();

        return ResponseEntity.ok(CustomModelMapper.toDtoList(assignees, AssigneeResponse.class));
    }
}
