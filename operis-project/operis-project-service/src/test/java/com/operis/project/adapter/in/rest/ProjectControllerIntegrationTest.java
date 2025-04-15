package com.operis.project.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.operis.project.adapter.in.rest.dto.ProjectDto;
import com.operis.project.adapter.in.rest.error.ApiError;
import com.operis.project.adapter.in.rest.payload.CreateProjectPayload;
import com.operis.project.adapter.out.persistence.JPAProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProjectControllerIntegrationTest {

    public static final String BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huLkRvZUBnbWFpbC5jb20iLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.fywlN-hAxA-NQNWyMyfxxRfPw1jvgJIQ7sYbJXgd130";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JPAProjectRepository jpaProjectRepository;

    @Test
    void createProjectShouldWorksSuccessfullyGivenValidParams() throws Exception {
        // Given
        CreateProjectPayload payload = new CreateProjectPayload(
                "Operis",
                "Project management platform"
        );

        // When
        MockHttpServletResponse response = mockMvc.perform(post("/api/projects")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", BEARER_TOKEN)
                    .content(objectMapper.writeValueAsString(payload)))
                // Then
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        // Then
        String responseString = response.getContentAsString();
        ProjectDto createProject = objectMapper.readValue(responseString, ProjectDto.class);

        assertNotNull(createProject.id());
        assertEquals("Operis", createProject.name());
        assertEquals("Project management platform", createProject.description());
        assertEquals("john.Doe@gmail.com", createProject.owner());

        // And
        jpaProjectRepository.findById(createProject.id())
                .ifPresentOrElse(
                        project -> {
                            assertEquals("Operis", project.getName());
                            assertEquals("Project management platform", project.getDescription());
                        },
                        () -> fail("Project not found in the database")
                );



    }

    @Test
    void createProjectShouldReturnBAD_REQUESTGivenEmptyProjectName() throws Exception {
        // Given
        CreateProjectPayload payload = new CreateProjectPayload(
                "  ",
                "Project management platform"
        );

        // When
        MockHttpServletResponse response = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_TOKEN)
                        .content(objectMapper.writeValueAsString(payload)))
                // Then
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        // And
        String responseString = response.getContentAsString();
        var apiError = objectMapper.readValue(responseString, ApiError.class);

        assertEquals(HttpStatus.BAD_REQUEST.name(), apiError.HttpStatus());
        assertEquals(HttpStatus.BAD_REQUEST.value(), apiError.statusCode());
        assertEquals("Validation failed for one or more fields", apiError.message());
    }
}