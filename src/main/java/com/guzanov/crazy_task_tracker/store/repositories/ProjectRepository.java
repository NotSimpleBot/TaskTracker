package com.guzanov.crazy_task_tracker.store.repositories;

import com.guzanov.crazy_task_tracker.store.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
}
