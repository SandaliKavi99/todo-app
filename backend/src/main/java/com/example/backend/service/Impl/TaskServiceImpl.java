package com.example.backend.service.Impl;

import com.example.backend.dto.TaskDto;
import com.example.backend.model.Task;
import com.example.backend.repository.TaskRepository;
import com.example.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public TaskDto saveTask(TaskDto taskDto) {
        Task task = new Task();

        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);

        return taskDto;
    }

    @Override
    public List<TaskDto> findTaskList() {
        List<Task> tasks = taskRepository.findTop5ByIsClosedFalseOrderByCreatedAtAsc();

        return tasks.stream()
                .map(task -> new TaskDto(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void updateTaskStatus(int taskId) {
        taskRepository.updateIsClosedAndUpdatedAtById(taskId, Boolean.TRUE, LocalDateTime.now());
    }

    @Override
    public void deleteTask(int taskId) {
        taskRepository.deleteById(taskId);
    }
}
