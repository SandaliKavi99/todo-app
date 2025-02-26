package com.example.backend.repository;

import com.example.backend.dto.TaskDto;
import com.example.backend.model.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryIT {

    @Autowired
    private TaskRepository taskRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void save_ShouldSaveTask(){
        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("New Description");
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);
        assertThat(savedTask.getId()).isNotNull();
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
            taskRepository.save(task);
        }

        List<Task> tasks = taskRepository.findTop5ByIsClosedFalseOrderByCreatedAtAsc();
        AssertionsForInterfaceTypes.assertThat(tasks).hasSize(5);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Task 0");
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

        entityManager.flush();
        entityManager.clear();

        Optional<Task> updatedTask = taskRepository.findById(task.getId());
        assertThat(updatedTask).isPresent();
        assertThat(updatedTask.get().getIsClosed()).isTrue();
    }
}
