package org.example.effectivemobiletask.service;

import org.example.effectivemobiletask.model.entity.Comment;
import org.example.effectivemobiletask.model.entity.User;

import java.util.List;

public interface CommentService {
    Comment createComment(Long taskId, User user, Comment comment);

    List<Comment> getComments(Long taskId);

    void deleteComment(Long taskId, User user, Long commentId);

    Comment updateComment(Long taskId, User user, Long commentId, Comment comment);
}
