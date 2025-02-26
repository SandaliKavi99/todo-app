package com.example.backend.repository;

import com.example.backend.BackendApplication;
import com.example.backend.model.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = BackendApplication.class)
public class TaskRepositoryUI {

    @Autowired
    private TaskRepository taskRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void save_ShouldSaveTask(){
        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("New Description");
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);
        entityManager.flush();

        assertThat(savedTask.getId()).isNotNull();
        Optional<Task> retrievedTask = taskRepository.findById(savedTask.getId());
        assertThat(retrievedTask).isPresent();
        assertThat(retrievedTask.get().getTitle()).isEqualTo("New Task");
    }

    @Test
    void findTop5ByIsClosedFalseOrderByCreatedAtAsc_ShouldReturnTask() {
        for(int i = 0; i<6; i++) {
            Task task = new Task();
            task.setTitle("Task " + i);
            task.setDescription("Description "+ i);
            task.setIsClosed(false);
            task.setCreatedAt(LocalDateTime.now());
            task.setUpdatedAt(LocalDateTime.now());
            entityManager.persist(task);
        }

        entityManager.flush();

        List<Task> tasks = taskRepository.findTop5ByIsClosedFalseOrderByCreatedAtAsc();
        AssertionsForInterfaceTypes.assertThat(tasks).hasSize(5);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Task 1");

    }

    @Test
    void updateIsClosedAndUpdatedAtById() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setIsClosed(false);
        task.setDescription("Test Description");
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);

        taskRepository.updateIsClosedAndUpdatedAtById(task.getId(), true, LocalDateTime.now());

        Task updatedTask = taskRepository.findById(task.getId()).orElseThrow();
        entityManager.refresh(updatedTask);
        assertThat(updatedTask.getIsClosed()).isTrue();

    }
}
