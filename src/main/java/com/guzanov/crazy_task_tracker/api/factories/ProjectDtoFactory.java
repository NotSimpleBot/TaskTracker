package com.guzanov.crazy_task_tracker.api.factories;

import com.guzanov.crazy_task_tracker.api.dto.ProjectDto;
import com.guzanov.crazy_task_tracker.store.entities.ProjectEntity;
import org.springframework.stereotype.Component;

/**
 * Получаем из табличного представления объекта - DTO представление (чисто данные)
 * Нужен что бы отдать на клиент не всю сущность, а то, что мы хотим
 */
@Component
public class ProjectDtoFactory {

    public ProjectDto makeProjectDto(ProjectEntity entity) {
        return ProjectDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .updateAt(entity.getUpdateAt())
                .build();
    }
}
