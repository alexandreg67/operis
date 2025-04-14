package com.operis.project.core.domain;

public record CreateProjectCommand(
        String name,
        String description,
        String owner
) {
}
