package com.guzanov.crazy_task_tracker.api.controllers;

import com.guzanov.crazy_task_tracker.api.dto.AskDto;
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
    public static final String DELETE_PROJECT = "/api/projects/{project_id}";
    public static final String CREATE_OR_UPDATE_PROJECT = "/api/projects/{project_id}";

    @PostMapping(CREATE_PROJECT) //не нужен из за createOrUpdateProject
    public ProjectDto createProject(@RequestParam String name) {
        if (name.trim().isEmpty()) {
            throw new BadRequestException("Name can't be empty.");
        }

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

    @PutMapping(CREATE_OR_UPDATE_PROJECT) //заменяет создание и обновление сущности
    ProjectDto createOrUpdateProject(@RequestParam(value = "project_name", required = false) Optional<String> projectName,
                                     @RequestParam(value = "project_id", required = false) Optional<Long> projectId) {
        projectName = projectName.filter(name -> !name.trim().isEmpty());
        boolean isCreate = projectId.isPresent();

        ProjectEntity projectEntity = projectId
                .map(this::getProjectOrThrowExeption)
                .orElseGet(ProjectEntity::new);

        if (isCreate && projectName.isEmpty()) {
            throw new BadRequestException("Name shouldn't be empty.");
        }

        projectName.ifPresent(name -> {
            projectRepository.findByName(name)
                    .filter(foundEntity -> !Objects.equals(foundEntity.getId(), projectEntity.getId()))
                    .ifPresent(foundEntity -> {
                        throw new BadRequestException("sad");
                    });
            projectEntity.setName(name);
        });
        final ProjectEntity savedProject = projectRepository.saveAndFlush(projectEntity);

        return projectDtoFactory.makeProjectDto(savedProject);
    }

    @PatchMapping(EDIT_PROJECT) //не нужен из за createOrUpdateProject
    public ProjectDto updateProject(@PathVariable Long projectId, @RequestParam String name) {
        if (name.trim().isEmpty()) {
            throw new BadRequestException("Name can't be empty.");
        }
        ProjectEntity project = getProjectOrThrowExeption(projectId);

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
        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty()); //?

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

    @DeleteMapping(DELETE_PROJECT)
    public AskDto deleteProject(@PathVariable Long projectId) {
        getProjectOrThrowExeption(projectId);
        projectRepository.deleteById(projectId);
        return AskDto.makeDefault(true);
    }

    private ProjectEntity getProjectOrThrowExeption(Long projectId) {
        return projectRepository
                .findById(projectId)
                .orElseThrow(() -> {
                            throw new NotFoundException(String.format("Project with id %s not find.", projectId));
                        }
                );
    }
    /*
     * Лучше принимать параметры руками с помощью @RequestParam, можно принимать и объект @RequestBody*/
}
