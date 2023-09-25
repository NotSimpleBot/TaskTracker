package com.guzanov.crazy_task_tracker.store.repositories;

import com.guzanov.crazy_task_tracker.store.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
