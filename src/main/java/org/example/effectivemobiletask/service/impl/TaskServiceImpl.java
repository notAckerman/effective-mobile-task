package org.example.effectivemobiletask.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletask.model.entity.Assignee;
import org.example.effectivemobiletask.model.entity.Task;
import org.example.effectivemobiletask.model.entity.User;
import org.example.effectivemobiletask.model.map.TaskStatus;
import org.example.effectivemobiletask.repository.TaskRepository;
import org.example.effectivemobiletask.service.AssigneeService;
import org.example.effectivemobiletask.service.TaskService;
import org.example.effectivemobiletask.service.UserService;
import org.example.effectivemobiletask.util.exception.AccessDeniedException;
import org.example.effectivemobiletask.util.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final AssigneeService assigneeService;

    @Override
    @Transactional
    public Task createTask(Task task, User user) {
        task.setCreationDate(LocalDateTime.now());
        task.setAuthor(user);
        task.setStatus(TaskStatus.PENDING);

        return taskRepository.save(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllTasks(String name) {
        User user = userService.getUser(name);
        return taskRepository.findAllByAuthor(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllAvailableTasks() {
        return taskRepository.findAllByStatus(TaskStatus.PENDING);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllAssignedTasks(User assignee) {
        return ((Assignee) assignee).getAssignedTasks();
    }

    @Override
    @Transactional(readOnly = true)
    public Task getTask(Long id, String name) {
        User user = userService.getUser(name);
        return taskRepository.findByIdAndAuthor(id, user).orElseThrow(
                () -> new NotFoundException("Task not found")
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Task getTask(Long id, User author) {
        return taskRepository.findByIdAndAuthor(id, author).orElseThrow(
                () -> new NotFoundException("Task not found")
        );
    }


    @Override
    @Transactional(readOnly = true)
    public Task getTask(Long id) {
        return taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Task not found")
        );
    }

    @Override
    @Transactional
    public void deleteTask(Long id, User author) {
        Task task = getTask(id, author);

        taskRepository.delete(task);
    }

    @Override
    @Transactional
    public Task updateTask(Long id, Task task, User author) {
        Task existingTask = getTask(id, author);

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setPriority(task.getPriority());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setAssignee(task.getAssignee());

        return taskRepository.save(existingTask);
    }

    @Override
    @Transactional
    public Task assignTask(Long id, User assignee) {
        Task task = getTask(id);

        if (task.getAssignee() != null) {
            throw new AccessDeniedException("Task already has an assignee.");
        }

        task.setAssignee((Assignee) assignee);

        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task updateStatus(Long id, User assignee, TaskStatus status) {
        Task task = getTask(id);

        if (!Objects.equals(assignee, task.getAssignee())) {
            throw new AccessDeniedException("You don't have permission to update the status of this task");
        }

        task.setStatus(status);

        return taskRepository.save(task);
    }
}
