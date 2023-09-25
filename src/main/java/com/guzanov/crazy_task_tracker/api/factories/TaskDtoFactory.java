package com.guzanov.crazy_task_tracker.api.factories;

import com.guzanov.crazy_task_tracker.api.dto.TaskDto;
import com.guzanov.crazy_task_tracker.store.entities.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoFactory {

    public TaskDto makeTaskDto(TaskEntity entity) {
        return TaskDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .description(entity.getDescription())
                .build();
    }
}
