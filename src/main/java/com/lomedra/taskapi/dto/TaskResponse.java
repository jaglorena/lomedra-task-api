package com.lomedra.taskapi.dto;

public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private boolean isCompleted;

    public TaskResponse(Long id, String title, String description, boolean isCompleted) {
        this.id =id;
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return  description;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }
}
