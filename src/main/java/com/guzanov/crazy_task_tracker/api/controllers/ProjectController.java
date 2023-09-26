package com.guzanov.crazy_task_tracker.api.controllers;

import com.guzanov.crazy_task_tracker.api.dto.ProjectDto;
import com.guzanov.crazy_task_tracker.api.exeptions.BadRequestException;
import com.guzanov.crazy_task_tracker.api.factories.ProjectDtoFactory;
import com.guzanov.crazy_task_tracker.store.entities.ProjectEntity;
import com.guzanov.crazy_task_tracker.store.repositories.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RequiredArgsConstructor //засовывет все поля в конструкто (и внедряет зависимость как @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) //штука хорошая, но эксперементальная
@Transactional //не очень хороший вариант, лучше на уровне методов
@RestController("/api")
public class ProjectController {

    ProjectRepository projectRepository;
    ProjectDtoFactory projectDtoFactory; //проинициализируется в конструкторе за счёт @RequiredArgsConstructor

    public static final String CREATE_PROJECT = "/projects";

    @PostMapping(CREATE_PROJECT)
    public ProjectDto createProject(@RequestParam String name) {
        projectRepository
                .findByName(name)
                .ifPresent (project -> {
                    throw new BadRequestException(String.format("Project \"%s\" already exist.", name));
                });
        ProjectEntity projectEntity = projectRepository.findByName(name).get();

        return null;
    }


    /*
     * Лучше принимать параметры руками с помощью @RequestParam, можно принимать и объект @RequestBody*/
}
