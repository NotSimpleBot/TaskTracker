package com.guzanov.crazy_task_tracker.store.repositories;

import com.guzanov.crazy_task_tracker.store.entities.TaskStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {
}
