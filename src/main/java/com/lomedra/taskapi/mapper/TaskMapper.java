package com.lomedra.taskapi.mapper;

import com.lomedra.taskapi.dto.TaskRequest;
import com.lomedra.taskapi.dto.TaskResponse;
import com.lomedra.taskapi.entity.Task;

public final class TaskMapper {
    private TaskMapper() {
    }

    public static Task toEntity(TaskRequest request) {
        return new Task(
                request.getTitle(),
                request.getDescriprion(),
                request.getIsCompleted()
        );
    }

    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted()
        );
    }
}
