package com.operis.project.core.domain;

import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
public class Project {
    private String id;
    private String name;
    private String description;
    private String owner;
    private List<String> members;

    public Project(String name, String description, String owner) {

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }

        if (owner == null || owner.isEmpty()) {
            throw new IllegalArgumentException("Project owner cannot be null or empty");
        }

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.members = List.of(owner);
    }


}
