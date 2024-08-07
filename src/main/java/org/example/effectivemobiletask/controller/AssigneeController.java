package org.example.effectivemobiletask.controller;

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
public class AssigneeController {

    private final AssigneeService assigneeService;

    @GetMapping
    public ResponseEntity<List<AssigneeResponse>> getAllAssignees() {
        List<Assignee> assignees = assigneeService.getAssignees();
        List<AssigneeResponse> response = CustomModelMapper.toDtoList(assignees, AssigneeResponse.class);
        return ResponseEntity.ok(response);
    }
}
