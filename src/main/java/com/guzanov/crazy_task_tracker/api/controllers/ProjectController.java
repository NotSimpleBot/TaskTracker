package com.guzanov.crazy_task_tracker.api.controllers;

import com.guzanov.crazy_task_tracker.api.factories.ProjectDtoFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RequiredArgsConstructor //засовывет все поля в конструкто (и внедряет зависимость как @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) //штука хорошая, но эксперементальная
@Transactional //не очень хороший вариант, лучше на уровне методов
@RestController
public class ProjectController {

    ProjectDtoFactory projectDtoFactory; //проинициализируется в конструкторе за счёт @RequiredArgsConstructor


}
