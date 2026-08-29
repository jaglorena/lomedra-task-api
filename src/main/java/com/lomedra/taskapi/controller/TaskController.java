package com.lomedra.taskapi.controller;

import com.lomedra.taskapi.dto.TaskRequest;
import com.lomedra.taskapi.dto.TaskResponse;
import com.lomedra.taskapi.entity.Task;
import com.lomedra.taskapi.mapper.TaskMapper;
import com.lomedra.taskapi.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> tasks = taskService.getAllTasks()
                .stream()
                .map(TaskMapper::toResponse)
                .toList();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long  id) {
        Task task = taskService.getTaskById(id);

        return ResponseEntity.ok(TaskMapper.toResponse(task));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask (@Valid @RequestBody TaskRequest request) {

        Task task = TaskMapper.toEntity(request);
        Task createdTask = taskService.createTask(task);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TaskMapper.toResponse(createdTask));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        Task taskDetails = TaskMapper.toEntity(request);
        Task updateTask= taskService.updateTask(id, taskDetails);

        return ResponseEntity.ok(TaskMapper.toResponse(updateTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletTask(@PathVariable Long id) {
        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }
}
