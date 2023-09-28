package com.guzanov.crazy_task_tracker.store.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) //?
@Entity
@Table(name = "project")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(unique = true)
    String name;

    @Builder.Default
    Instant createdAt = Instant.now();

    @Builder.Default
    Instant updateAt = Instant.now();

    @Builder.Default
    @OneToMany
    @JoinColumn(name = "project_id", referencedColumnName = "id") // (FC) в таблице TaskStateEntity будет создана колонка project_id и будет ссылкать на id таблицы Project (можно не указывать id)
    List<TaskStateEntity> taskStates = new ArrayList<>();
}
