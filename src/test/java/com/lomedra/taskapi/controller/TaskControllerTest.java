package com.lomedra.taskapi.controller;

import com.lomedra.taskapi.entity.Task;
import com.lomedra.taskapi.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    void ListTasks() throws Exception {
        taskRepository.save(new Task(
                "First task",
                "First task description",
                false
        ));

        taskRepository.save(new Task(
                "Second task",
                "Second task description",
                true
        ));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("First task"))
                .andExpect(jsonPath("$[1].title").value("Second task"));
    }

    @Test
    void UpdateTask() throws Exception {
        Task task = taskRepository.save(new Task(
                "Original task",
                "Original description",
                false
        ));

        String requestBody = """
            {
              "title": "Updated task",
              "description": "Updated description",
              "isCompleted": true
            }
            """;

        mockMvc.perform(put("/api/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value("Updated task"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.isCompleted").value(true));
    }

    @Test
    void DeleteTask() throws Exception {
        Task task = taskRepository.save(new Task(
                "Task to delete",
                "Delete this task",
                false
        ));

        mockMvc.perform(delete("/api/tasks/{id}", task.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks/{id}", task.getId()))
                .andExpect(status().isNotFound());
    }
}
