package com.lomedra.taskapi.service;

import com.lomedra.taskapi.entity.Task;
import com.lomedra.taskapi.exception.TaskNotFoundException;
import com.lomedra.taskapi.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task updateTask(Long id, Task tDetails) {
        Task existingTask = getTaskById(id);

        existingTask.setTitle(tDetails.getTitle());
        existingTask.setDescription(tDetails.getDescription());
        existingTask.setCompleted(tDetails.isCompleted());

        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id) {
        Task existingTask = getTaskById(id);
        taskRepository.delete(existingTask);
    }
}
