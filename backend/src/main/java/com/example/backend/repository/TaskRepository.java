package com.example.backend.repository;

import com.example.backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    //get 5 tasks by ascending order
    List<Task> findTop5ByIsClosedFalseOrderByCreatedAtAsc();

    //update isclosed status
    @Modifying
    @Transactional
    @Query(value = "UPDATE task t SET t.isclosed = :isClosed, t.updated_at = :updatedAt WHERE t.id = :taskId", nativeQuery = true)
    void updateIsClosedAndUpdatedAtById(int taskId, Boolean isClosed, LocalDateTime updatedAt);

}
