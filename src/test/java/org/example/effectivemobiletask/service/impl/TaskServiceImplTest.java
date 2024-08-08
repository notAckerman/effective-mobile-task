package org.example.effectivemobiletask.service.impl;

import org.example.effectivemobiletask.model.entity.Assignee;
import org.example.effectivemobiletask.model.entity.Task;
import org.example.effectivemobiletask.model.entity.User;
import org.example.effectivemobiletask.model.map.TaskPriority;
import org.example.effectivemobiletask.model.map.TaskStatus;
import org.example.effectivemobiletask.repository.TaskRepository;
import org.example.effectivemobiletask.service.AssigneeService;
import org.example.effectivemobiletask.service.UserService;
import org.example.effectivemobiletask.util.exception.AccessDeniedException;
import org.example.effectivemobiletask.util.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @Mock
    private AssigneeService assigneeService;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private User user;

    private Assignee assignee;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("testuser");

        assignee = new Assignee();
        assignee.setId(2L);
        assignee.setName("testassignee");

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setAssignee(assignee);
        task.setPriority(TaskPriority.LOW);
        task.setDueDate(LocalDateTime.now().plusDays(1));
        task.setStatus(TaskStatus.PENDING);
        task.setAuthor(user);
    }

    @Test
    void createTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(task, user);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void getAllTasks() {
        when(userService.getUser(anyString())).thenReturn(user);
        when(taskRepository.findAllByAuthor(any(User.class))).thenReturn(List.of(task));

        List<Task> result = taskService.getAllTasks("testuser");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Task", result.get(0).getTitle());
        verify(userService, times(1)).getUser("testuser");
        verify(taskRepository, times(1)).findAllByAuthor(user);
    }

    @Test
    void getAllAvailableTasks() {
        when(taskRepository.findAllByStatus(any(TaskStatus.class))).thenReturn(List.of(task));

        List<Task> result = taskService.getAllAvailableTasks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Task", result.get(0).getTitle());
        verify(taskRepository, times(1)).findAllByStatus(TaskStatus.PENDING);
    }


    @Test
    void getTask() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        Task result = taskService.getTask(1L);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskWithUser() {
        when(userService.getUser(anyString())).thenReturn(user);
        when(taskRepository.findByIdAndAuthor(anyLong(), any(User.class))).thenReturn(Optional.of(task));

        Task result = taskService.getTask(1L, "testuser");

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(userService, times(1)).getUser("testuser");
        verify(taskRepository, times(1)).findByIdAndAuthor(1L, user);
    }

    @Test
    void getTaskNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> taskService.getTask(1L));

        assertEquals("Task not found", exception.getMessage());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void deleteTask() {
        when(taskRepository.findByIdAndAuthor(anyLong(), any(User.class))).thenReturn(Optional.of(task));

        taskService.deleteTask(1L, user);

        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void updateTask() {
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setPriority(TaskPriority.MID);
        updatedTask.setDueDate(LocalDateTime.now().plusDays(5));
        updatedTask.setAssignee(assignee);

        when(taskRepository.findByIdAndAuthor(anyLong(), any(User.class))).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.updateTask(1L, updatedTask, user);

        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
        verify(taskRepository, times(1)).save(updatedTask);
    }

    @Test
    void assignTask() {
        task.setAssignee(null);

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.assignTask(1L, assignee);

        assertNotNull(result);
        assertEquals(assignee, result.getAssignee());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void assignTaskAlreadyAssigned() {
        task.setAssignee(assignee);

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> taskService.assignTask(1L, assignee));

        assertEquals("Task already has an assignee.", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void updateStatus() {
        TaskStatus newStatus = TaskStatus.COMPLETED;

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.updateStatus(1L, ((User) assignee), newStatus);

        assertNotNull(result);
        assertEquals(newStatus, result.getStatus());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void updateStatusAccessDenied() {
        User differentAssignee = new User();
        differentAssignee.setId(3L);

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> taskService.updateStatus(1L, differentAssignee, TaskStatus.COMPLETED));

        assertEquals("You don't have permission to update the status of this task", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }
}
