package com.example.backend.service.Impl;

import com.example.backend.dto.TaskDto;
import com.example.backend.model.Task;
import com.example.backend.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TaskServiceImplIT {

    @Autowired
    private TaskServiceImpl taskService;

    @Autowired
    private TaskRepository taskRepository;

    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        taskDto = new TaskDto();
        taskDto.setTitle("Service Layer Task");
        taskDto.setDescription("Service Layer Description");
    }


    @Test
    void saveTask_ShouldSaveAndReturnTaskDto() {
        TaskDto savedTask = taskService.saveTask(taskDto);
        assertNotNull(savedTask);
        assertEquals("Service Layer Task", savedTask.getTitle());
    }

    @Test
    void findTaskList_ShouldReturnListOfTaskDtosWithIsClosedFalse() {
        Task task = new Task();
        task.setTitle("Task 1");
        task.setDescription("Description1");
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);

        List<TaskDto> result = taskService.findTaskList();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Task 1", result.get(0).getTitle());


    }

    @Test
    void updateTaskStatus_ShouldUpdateTaskIsClosedStatus() {
        Task task = new Task();
        task.setTitle("Task Close");
        task.setDescription("Description Close");
        task.setIsClosed(false);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);

        taskService.updateTaskStatus(task.getId());

        Task updatedTask = taskRepository.findById(task.getId()).orElseThrow();
        assertTrue(updatedTask.getIsClosed());

    }

}
