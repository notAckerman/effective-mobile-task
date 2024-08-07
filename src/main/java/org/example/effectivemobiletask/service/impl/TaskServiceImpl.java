package org.example.effectivemobiletask.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletask.exception.AccessDeniedException;
import org.example.effectivemobiletask.exception.NotFoundException;
import org.example.effectivemobiletask.model.entity.Assignee;
import org.example.effectivemobiletask.model.entity.Task;
import org.example.effectivemobiletask.model.entity.User;
import org.example.effectivemobiletask.model.map.TaskStatus;
import org.example.effectivemobiletask.repository.TaskRepository;
import org.example.effectivemobiletask.service.TaskService;
import org.example.effectivemobiletask.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Override
    public Task createTask(Task task, User user) {
        task.setCreationDate(LocalDateTime.now());
        task.setAuthor(user);
        task.setStatus(TaskStatus.PENDING);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks(String name) {
        User user = userService.getUser(name);
        return taskRepository.findAllByAuthor(user);
    }

    @Override
    public List<Task> getAllAvailableTasks() {
        return taskRepository.findAllByStatus(TaskStatus.PENDING);
    }

    @Override
    public List<Task> getAllAssignedTasks(User user) {
        return ((Assignee) user).getAssignedTasks();
    }

    @Override
    public Task getTask(Long id, String name) {
        User user = userService.getUser(name);
        return taskRepository.findByIdAndAuthor(id, user).orElseThrow(
                () -> new NotFoundException("Task not found")
        );
    }

    @Override
    public Task getTask(Long id, User author) {
        return taskRepository.findByIdAndAuthor(id, author).orElseThrow(
                () -> new NotFoundException("Task with not found")
        );
    }


    @Override
    public Task getTask(Long id) {
        return taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Task not found")
        );
    }

    @Override
    public void deleteTask(Long id, User author) {
        Task task = getTask(id, author);
        taskRepository.delete(task);
    }

    @Override
    public Task updateTask(Long id, Task task, User author) {
        Task taskToUpdate = getTask(id, author);
        taskToUpdate.setTitle(task.getTitle());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setPriority(task.getPriority());
        taskToUpdate.setDueDate(task.getDueDate());
        taskToUpdate.setAssignee(task.getAssignee());
        return taskRepository.save(taskToUpdate);
    }

    @Override
    public Task assignTask(Long id, User user) {
        Task task = getTask(id);
        if (task.getAssignee() != null) {
            throw new AccessDeniedException("Task already has an assignee.");
        }
        task.setAssignee((Assignee) user);
        return taskRepository.save(task);
    }

    @Override
    public Task updateStatus(Long id, User assignee, TaskStatus status) {
        Task task = getTask(id);
        if (!Objects.equals(assignee, task.getAssignee())) {
            throw new AccessDeniedException("You don't have permission to update the status of this task");
        }
        task.setStatus(status);
        return taskRepository.save(task);
    }
}
