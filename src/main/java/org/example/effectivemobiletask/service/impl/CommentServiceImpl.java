package org.example.effectivemobiletask.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletask.exception.AccessDeniedException;
import org.example.effectivemobiletask.exception.NotFoundException;
import org.example.effectivemobiletask.model.entity.Comment;
import org.example.effectivemobiletask.model.entity.Task;
import org.example.effectivemobiletask.model.entity.User;
import org.example.effectivemobiletask.repository.CommentRepository;
import org.example.effectivemobiletask.service.CommentService;
import org.example.effectivemobiletask.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final TaskService taskService;

    private final CommentRepository commentRepository;

    @Override
    public Comment createComment(Long taskId, User user, Comment comment) {
        Task task = taskService.getTask(taskId);
        comment.setAuthor(user);
        comment.setCreationDate(LocalDateTime.now());
        task.addComment(comment);
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getComments(Long taskId) {
        Task task = taskService.getTask(taskId);
        return task.getComments();
    }

    @Override
    public void deleteComment(Long taskId, User user, Long commentId) {
        Task task = taskService.getTask(taskId);
        Comment comment = commentRepository.findByIdAndTask(commentId, task).orElseThrow(
                () -> new NotFoundException("Comment not found")
        );
        task.removeComment(comment);
        commentRepository.delete(comment);
    }

    @Override
    public Comment updateComment(Long taskId, User user, Long commentId, Comment commentRequest) {
        Task task = taskService.getTask(taskId);
        Comment existingComment = commentRepository.findByIdAndTask(commentId, task).orElseThrow(
                () -> new NotFoundException("Comment not found")
        );

        if (!existingComment.getAuthor().equals(user)) {
            throw new AccessDeniedException("You don't have permission to update this comment");
        }

        existingComment.setText(commentRequest.getText());

        return commentRepository.save(existingComment);
    }
}
