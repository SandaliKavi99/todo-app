package com.example.backend.controller;

import com.example.backend.dto.TaskDto;
import com.example.backend.model.Task;
import com.example.backend.repository.TaskRepository;
import com.example.backend.service.Impl.TaskServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TaskControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    private TaskDto taskDto;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();

        taskDto = new TaskDto();
        taskDto.setTitle("Integration Test Task");
        taskDto.setDescription("Integration Test Description");
    }

    @Test
    void createTask_ShouldReturnSuccessMessage() throws Exception {

        mockMvc.perform(post("/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDto)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.message").value("Successfully Added"));

    }

    @Test
    void getTaskList_ShouldReturnListOfTasks() throws Exception {
        Task task = new Task();
        task.setTitle("Integration Test Task1");
        task.setDescription("Integration Test Description1");
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);

        mockMvc.perform(get("/task/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Integration Test Task1"));

    }

    @Test
    void getTaskList_ShouldReturnEmptyList() throws Exception {

        mockMvc.perform(get("/task/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void updateTaskAsCompleted_ShouldReturnSuccessMessage() throws Exception {
        Task task = new Task();
        task.setTitle("Integration Test Task1");
        task.setDescription("Integration Test Description1");
        task.setIsClosed(false);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);

        mockMvc.perform(put("/task/close/"+ task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully Updated"));

        Task updatedTask = taskRepository.findById(task.getId()).orElseThrow();
        assert updatedTask.getIsClosed();
    }

    @Test
    void updateTaskAsCompleted_ShouldFailForNotExistTask() throws Exception {

        mockMvc.perform(put("/task/close/-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully Updated"));

    }
}
