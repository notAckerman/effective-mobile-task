package org.example.effectivemobiletask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletask.model.dto.task.CommentRequest;
import org.example.effectivemobiletask.model.dto.task.CommentResponse;
import org.example.effectivemobiletask.model.entity.Comment;
import org.example.effectivemobiletask.model.entity.User;
import org.example.effectivemobiletask.service.CommentService;
import org.example.effectivemobiletask.util.CustomModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{username}/tasks/{task-id}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable("task-id") Long taskId, @PathVariable String username) {
        List<Comment> comments = commentService.getComments(taskId);
        List<CommentResponse> response = CustomModelMapper.toDtoList(comments, CommentResponse.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@PathVariable("username") String username,
                                                         @PathVariable("task-id") Long taskId,
                                                         @Valid @RequestBody CommentRequest commentRequest) {
        User user = getUser();
        Comment comment = CustomModelMapper.toEntity(Comment.class, commentRequest);
        comment = commentService.createComment(taskId, user, comment);
        CommentResponse response = CustomModelMapper.toDto(CommentResponse.class, comment);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{comment-id}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable("username") String username,
                                                         @PathVariable("task-id") Long taskId,
                                                         @PathVariable("comment-id") Long commentId,
                                                         @Valid @RequestBody CommentRequest commentRequest) {
        User user = getUser();
        Comment comment = CustomModelMapper.toEntity(Comment.class, commentRequest);
        comment = commentService.updateComment(taskId, user, commentId, comment);
        CommentResponse response = CustomModelMapper.toDto(CommentResponse.class, comment);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("username") String username,
                                              @PathVariable("task-id") Long taskId,
                                              @PathVariable("comment-id") Long commentId) {
        User user = getUser();
        commentService.deleteComment(taskId, user, commentId);
        return ResponseEntity.noContent().build();
    }

    private static User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
