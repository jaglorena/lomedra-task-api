package com.lomedra.taskapi.dto;

import jakarta.validation.constraints.NotBlank;

public class TaskRequest {
    @NotBlank(message = "Title is required and cannot be empty")
    private String title;
    private String description;
    private boolean isCompleted;

    public TaskRequest() {
    }

    public TaskRequest(String title, String description, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
    }
     public String getTitle() {
        return title;
     }

     public String getDescription() {
        return description;
     }

     public boolean getIsCompleted() {
        return isCompleted;
     }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
