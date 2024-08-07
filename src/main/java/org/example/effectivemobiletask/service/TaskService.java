package org.example.effectivemobiletask.service;

import org.example.effectivemobiletask.model.entity.Task;
import org.example.effectivemobiletask.model.entity.User;
import org.example.effectivemobiletask.model.map.TaskStatus;

import java.util.List;

public interface TaskService {
    Task createTask(Task task, User author);

    List<Task> getAllTasks(String authorUsername);

    List<Task> getAllAvailableTasks();

    List<Task> getAllAssignedTasks(User assignee);

    Task getTask(Long id, String authorUsername);

    Task getTask(Long id, User author);

    Task getTask(Long id);

    void deleteTask(Long id, User author);

    Task updateTask(Long id, Task task, User author);

    Task assignTask(Long id, User assignee);

    Task updateStatus(Long id, User assignee, TaskStatus status);
}
