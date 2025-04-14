package com.operis.project.adapter.in.rest;

import com.operis.project.adapter.in.rest.dto.ProjectDto;
import com.operis.project.adapter.in.rest.payload.CreateProjectPayload;
import com.operis.project.core.domain.CreateProjectCommand;
import com.operis.project.core.domain.Project;
import com.operis.project.core.port.in.CreateProjectUseCase;
import com.operis.project.infrastructure.jwt.JWTConnectedUserResolver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final JWTConnectedUserResolver jwtConnectedUserResolver;

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @Valid @RequestBody CreateProjectPayload payload,
            @RequestHeader("Authorization") String authorizationHeader) {

        String connectedUser = jwtConnectedUserResolver.extractConnectedUserEmail(authorizationHeader);

        CreateProjectCommand command = new CreateProjectCommand(
                payload.name(),
                payload.description(),
                connectedUser
        );
        Project project = createProjectUseCase.createProject(command);
        return new ResponseEntity<>(
                new ProjectDto(project.getId(), project.getName(), project.getDescription(), project.getOwner()),
                HttpStatus.CREATED
        );

    }
}
