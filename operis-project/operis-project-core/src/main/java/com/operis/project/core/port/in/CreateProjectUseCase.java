package com.operis.project.core.port.in;

import com.operis.project.core.domain.CreateProjectCommand;
import com.operis.project.core.domain.Project;

public interface CreateProjectUseCase {

    public Project createProject(CreateProjectCommand command);
}
