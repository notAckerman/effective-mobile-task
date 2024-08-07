package org.example.effectivemobiletask.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.effectivemobiletask.model.map.UserRole;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Assignee extends User {

    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Task> assignedTasks;

    private Long completed;

    public Assignee(String email, String name, String password, UserRole role, List<Task> tasks, List<Task> assignedTasks, Long completed) {
        super(email, name, password, role, tasks);
        this.assignedTasks = assignedTasks;
        this.completed = completed;
    }
}
