package org.example.effectivemobiletask.repository;

import org.example.effectivemobiletask.model.entity.Comment;
import org.example.effectivemobiletask.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndTask(Long id, Task task);
}
