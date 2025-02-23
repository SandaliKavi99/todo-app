package com.example.backend.service;
import com.example.backend.dto.TaskDto;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {

    public TaskDto saveTask(TaskDto task);
    public List<TaskDto> findTaskList();
    public void updateTaskStatus(int taskId);
    public void deleteTask(int taskId);
}
