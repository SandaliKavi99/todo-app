package com.example.backend.controller;

import com.example.backend.dto.MessageResponseDto;
import com.example.backend.dto.TaskDto;
import com.example.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/task")
@CrossOrigin
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskDto task){
       try{
           taskService.saveTask(task);
           return ResponseEntity.ok().body(new MessageResponseDto("Successfully Added", true));
       } catch (Exception e){
           return ResponseEntity.badRequest().body(new MessageResponseDto(e.getMessage(), false));
       }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getTaskList(){
        try{
            List<TaskDto> tasks = taskService.findTaskList();
            return ResponseEntity.ok().body(tasks);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(new MessageResponseDto(e.getMessage(), false));
        }
    }

    @PutMapping("/close/{id}")
    public ResponseEntity<?> updateTaskAsCompleted(@PathVariable("id") int taskId){
        try{
            taskService.updateTaskStatus(taskId);
            return ResponseEntity.ok().body(new MessageResponseDto("Successfully Updated", true));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(new MessageResponseDto(e.getMessage(), false));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> RemoveTask(@PathVariable("id") int taskId){
        try{
            taskService.deleteTask(taskId);
            return ResponseEntity.ok().body(new MessageResponseDto("Successfully Deleted", true));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(new MessageResponseDto(e.getMessage(), false));
        }
    }




}
