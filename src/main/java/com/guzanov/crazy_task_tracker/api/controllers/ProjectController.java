package com.guzanov.crazy_task_tracker.api.controllers;

import com.guzanov.crazy_task_tracker.api.dto.ProjectDto;
import com.guzanov.crazy_task_tracker.api.exeptions.BadRequestException;
import com.guzanov.crazy_task_tracker.api.exeptions.NotFoundException;
import com.guzanov.crazy_task_tracker.api.factories.ProjectDtoFactory;
import com.guzanov.crazy_task_tracker.store.entities.ProjectEntity;
import com.guzanov.crazy_task_tracker.store.repositories.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor //засовывет все поля в конструкто (и внедряет зависимость как @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) //штука хорошая, но эксперементальная
@Transactional //не очень хороший вариант, лучше на уровне методов
@RestController
public class ProjectController {

    ProjectRepository projectRepository;
    ProjectDtoFactory projectDtoFactory; //проинициализируется в конструкторе за счёт @RequiredArgsConstructor

    public static final String CREATE_PROJECT = "/api/projects";
    public static final String EDIT_PROJECT = "/api/projects/{project_id}";
    public static final String FETCH_PROJECTS = "/api/projects/{project_id}";

    @PostMapping(CREATE_PROJECT)
    public ProjectDto createProject(@RequestParam String name) {
        if (name.trim().isEmpty()) {
            throw new BadRequestException("Name can't be empty.");
        }

        String name1 = "asd";
        projectRepository
                .findByName(name)
                .ifPresent(project -> {
                    throw new BadRequestException(String.format("Project \"%s\" already exist.", name));
                });

        //при обычном save не происзодит генерация ID, нужен flush, что бы сразу получить ID
        ProjectEntity projectEntity = projectRepository.save(
                ProjectEntity.builder()
                        .name(name)
                        .build()
        );

        return projectDtoFactory.makeProjectDto(projectEntity);
    }

    @PatchMapping(EDIT_PROJECT)
    public ProjectDto updateProject(@PathVariable Long projectId, @RequestParam String name) {
        if (name.trim().isEmpty()) {
            throw new BadRequestException("Name can't be empty.");
        }
        ProjectEntity project = projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Project with %s doesn't exist.", projectId))
                );

        projectRepository.findByName(name)
                .filter(anotherProject -> !Objects.equals(anotherProject.getId(), projectId))
                .ifPresent(anotherProject -> {
                            throw new BadRequestException(String.format("Project \"%s\" already exist.", name));
                        }
                );

        project.setName(name);
        project = projectRepository.save(project);

        return projectDtoFactory.makeProjectDto(project);
    }


    @GetMapping(FETCH_PROJECTS)
    public List<ProjectDto> fetchProject(@RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {
        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

        Stream<ProjectEntity> projectStream = optionalPrefixName
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)//если представлен Optional
                .orElseGet(projectRepository::streamAll);//если не представлен
       /* if (optionalPrefixName.isPresent()) {
            projectStream = projectRepository.streamAllByNameStartsWithIgnoreCase(optionalPrefixName.get());
        } else {
            projectStream = projectRepository.streamAll();
        }*/

        return projectStream
                .map(projectDtoFactory::makeProjectDto)
                .collect(Collectors.toList());
    }

    /*
     * Лучше принимать параметры руками с помощью @RequestParam, можно принимать и объект @RequestBody*/
}
