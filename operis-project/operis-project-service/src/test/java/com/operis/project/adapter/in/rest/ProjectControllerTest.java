package com.operis.project.adapter.in.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.operis.project.adapter.in.rest.payload.CreateProjectPayload;
import com.operis.project.core.domain.Project;
import com.operis.project.core.port.in.CreateProjectUseCase;
import com.operis.project.infrastructure.jwt.JWTConnectedUserResolver;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateProjectUseCase createProjectUseCase;

    @MockBean
    private JWTConnectedUserResolver jwtConnectedUserResolver;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createProjectShouldReturnCREATED() throws Exception {

        // Given
        CreateProjectPayload payload = new CreateProjectPayload(
                "Operis",
                "Project management platform"
        );

        Mockito.when(jwtConnectedUserResolver.extractConnectedUserEmail(Mockito.any()))
                        .thenReturn("john.Doe@gmail.com");

        Mockito.when(createProjectUseCase.createProject(Mockito.any()))
                        .thenReturn(new Project("operis", "Project management platform", "john.Doe@gmail.com"));

        // When + Then
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer dummy-token")
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated());
    }
}