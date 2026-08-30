package com.lomedra.taskapi.controller;

import com.lomedra.taskapi.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    private final MockMvc mockMvc;
    private final TaskRepository taskRepository;

    @Autowired
    TaskControllerTest(MockMvc mockMvc, TaskRepository taskRepository) {
        this.mockMvc = mockMvc;
        this.taskRepository = taskRepository;
    }

    @BeforeEach
    void cleanDatabase() {
        taskRepository.deleteAll();
    }

    @Test
    void CreateTask() throws Exception {
            String requestBody = """
            {
                "title" : "Finish REST API",
                "description" : "Complete the technical test",
                "isCompleted" : false
            }
            """;
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Finish REST API"))
                .andExpect(jsonPath("$.description").value("Complete the technical test"))
                .andExpect(jsonPath("$.isCompleted").value(false));
    }

    @Test
    void RejectBlankTitle400() throws Exception {
        String requestBody = """
                {
                    "title": "",
                    "description": "Task without title",
                    "isCompleted": false
                }
                """;
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.title").value("Title is required and cannot be empty"));
    }

    @Test
    void ReturnMissingTask404() throws Exception {
        long missingId = 123L;

        mockMvc.perform(get("/api/tasks/{id}", missingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Task with id "+ missingId +" was not found"));

    }
}
