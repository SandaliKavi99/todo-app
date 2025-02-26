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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplUT {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTitle("Test Title");
        task.setDescription("Test Description");
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());


        taskDto = new TaskDto();
        taskDto.setTitle("Test Title");
        taskDto.setDescription("Test Description");
    }

    @Test
    void saveTask_ShouldSaveAndReturnTaskDto() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        TaskDto savedTask = taskService.saveTask(taskDto);

        assertNotNull(savedTask);
        assertEquals(task.getId(), savedTask.getId());
        assertEquals(task.getTitle(), savedTask.getTitle());
    }

    @Test
    void findTaskList_ShouldReturnListOfTaskDtosWithIsClosedFalse() {
        List<Task> taskList = Arrays.asList(task);
        when(taskRepository.findTop5ByIsClosedFalseOrderByCreatedAtAsc()).thenReturn(taskList);

        List<TaskDto> result = taskService.findTaskList();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).getTitle());


    }

    @Test
    void updateTaskStatus_ShouldUpdateTaskIsClosedStatus() {
        doNothing().when(taskRepository).updateIsClosedAndUpdatedAtById(anyInt(), anyBoolean(), any(LocalDateTime.class));

        taskService.updateTaskStatus(1);
        verify(taskRepository, times(1)).updateIsClosedAndUpdatedAtById(eq(1), eq(true), any(LocalDateTime.class));

    }

}