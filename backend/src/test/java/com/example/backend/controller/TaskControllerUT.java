package com.example.backend.controller;

import com.example.backend.dto.TaskDto;
import com.example.backend.service.Impl.TaskServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Not support @MockBean

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TaskControllerUT {

    private MockMvc mockMvc;

    @Mock
    private TaskServiceImpl taskService; // Mocking TaskService

    @InjectMocks
    private TaskController taskController; // Injecting mock into controller

    private TaskDto taskDto;
    private TaskDto taskDto1;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        taskDto = new TaskDto();
        taskDto.setTitle("Test Title");
        taskDto.setDescription("Test Description");

        taskDto1 = new TaskDto();
        taskDto1.setTitle("Test Title1");
        taskDto1.setDescription("Test Description1");


    }

    @Test
    void createTask_ShouldReturnSuccessMessage() throws Exception {

        when(taskService.saveTask(any(TaskDto.class))).thenReturn(taskDto);

        mockMvc.perform(post("/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully Added"));


        verify(taskService, times(1)).saveTask(any(TaskDto.class));

    }

    @Test
    void getTaskList_ShouldReturnListOfTasks() throws Exception {
        List<TaskDto> taskList = Arrays.asList(taskDto1);

        when(taskService.findTaskList()).thenReturn(taskList);

        mockMvc.perform(get("/task/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Title1"));

        verify(taskService, times(1)).findTaskList();
    }

    @Test
    void getTaskList_ShouldReturnEmptyList() throws Exception {
        when(taskService.findTaskList()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/task/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void updateTaskAsCompleted_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(taskService).updateTaskStatus(1);

        mockMvc.perform(put("/task/close/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully Updated"));
        verify(taskService, times(1)).updateTaskStatus(1);
    }

    @Test
    void updateTaskAsCompleted_ShouldFailForNonExistTask() throws Exception {
            doThrow(new RuntimeException("Task not found")).when(taskService).updateTaskStatus(-1);

            mockMvc.perform(put("/task/close/-1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Task not found"));
        }
}
