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


    public ProjectEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<TaskStateEntity> getTaskStateEntityList() {
        return taskStateEntityList;
    }

    public void setTaskStateEntityList(List<TaskStateEntity> taskStateEntityList) {
        this.taskStateEntityList = taskStateEntityList;
    }
}
