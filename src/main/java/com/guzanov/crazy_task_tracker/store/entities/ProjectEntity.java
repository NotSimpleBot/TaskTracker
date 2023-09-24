package com.guzanov.crazy_task_tracker.store.entities;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "project")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String name;

    private Instant createdAt = Instant.now();

    @OneToMany
    private List<TaskStateEntity> taskStateEntityList;
}
