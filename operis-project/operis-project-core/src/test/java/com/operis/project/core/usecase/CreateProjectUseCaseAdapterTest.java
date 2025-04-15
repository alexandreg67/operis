package com.operis.project.core.usecase;

import com.operis.project.core.domain.CreateProjectCommand;
import com.operis.project.core.domain.Project;
import com.operis.project.core.port.out.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateProjectUseCaseAdapterTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private CreateProjectUseCaseAdapter createProjectUseCaseAdapter;

    @Test
    void createProjectShouldWorksSuccessfullyGivenValidParams() {

        // Given
        CreateProjectCommand command = new CreateProjectCommand(
                "operis",
                "Project management tool",
                "john.doe@gmail.com"
        );

        // When
        createProjectUseCaseAdapter.createProject(command);

        // Then
        ArgumentCaptor<Project> projectArgumentCaptor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).create(projectArgumentCaptor.capture());
        Project projectCapture = projectArgumentCaptor.getValue();
        assertThat(projectCapture.getId()).isNotNull();
        assertThat(projectCapture.getName()).isEqualTo("operis");
        assertThat(projectCapture.getDescription()).isEqualTo("Project management tool");
        assertThat(projectCapture.getOwner()).isEqualTo("john.doe@gmail.com");
        assertThat(projectCapture.getMembers()).containsExactlyInAnyOrder("john.doe@gmail.com");

    }

    @ParameterizedTest
    @CsvSource({
            "'  ', john.doe@gmail.com, Project name cannot be null or empty",
            "'', john.doe@gmail.com, Project name cannot be null or empty",
            ", john.doe@gmail.com, Project name cannot be null or empty",
            "operis, '  ', Project owner cannot be null or empty",
            "operis, '', Project owner cannot be null or empty",
            "operis, , Project owner cannot be null or empty",
    })
    void createProjectShouldThrowErrorGivenInvalidParams(
            String name,
            String owner,
            String expectedErrorMessage
    ) {

        // Given
        CreateProjectCommand command = new CreateProjectCommand(
                name,
                "Project management tool",
                owner
        );

        // When
        IllegalArgumentException actualException = assertThrows(IllegalArgumentException.class, () -> {
            createProjectUseCaseAdapter.createProject(command);
        });

        // Then
        assertThat(actualException.getMessage()).isEqualTo(expectedErrorMessage);

    }
}