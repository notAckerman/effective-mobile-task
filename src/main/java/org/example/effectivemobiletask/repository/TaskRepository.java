package org.example.effectivemobiletask.repository;

import org.example.effectivemobiletask.model.entity.Assignee;
import org.example.effectivemobiletask.model.entity.Task;
import org.example.effectivemobiletask.model.entity.User;
import org.example.effectivemobiletask.model.map.TaskStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findAllByAuthor(User author);

    Optional<Task> findByIdAndAuthor(Long id, User author);

    List<Task> findAllByStatus(TaskStatus status);
}
