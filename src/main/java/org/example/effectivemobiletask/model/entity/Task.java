package org.example.effectivemobiletask.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.effectivemobiletask.model.map.TaskPriority;
import org.example.effectivemobiletask.model.map.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private Assignee assignee;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public void addComment(Comment comment) {
        comment.setTask(this);
        comments.add(comment);
    }

    public void removeComment(Comment comment) {
        comment.setTask(null);
        comments.remove(comment);
    }
}