package org.example.effectivemobiletask.service;

import org.example.effectivemobiletask.model.entity.Assignee;

import java.util.List;

public interface AssigneeService {
    Assignee getAssignee(Long id);

    List<Assignee> getAssignees();
}
