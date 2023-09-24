package com.guzanov.crazy_task_tracker.store.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "task")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private Instant createdAt = Instant.now();

    private String description;
}
