package org.example.effectivemobiletask.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletask.util.exception.NotFoundException;
import org.example.effectivemobiletask.model.entity.Assignee;
import org.example.effectivemobiletask.repository.AssigneeRepository;
import org.example.effectivemobiletask.service.AssigneeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssigneeServiceImpl implements AssigneeService {

    private final AssigneeRepository assigneeRepository;

    @Override
    public Assignee getAssignee(Long id) {
        return assigneeRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Assignee not found")
        );
    }

    @Override
    public List<Assignee> getAssignees() {
        return assigneeRepository.findAll();
    }
}
