package com.lomedra.taskapi.dto;

import jakarta.validation.constraints.NotBlank;

public class TaskRequest {
    @NotBlank(message = "Title is required and cannot be empty")
    private String title;
    private String descriprion;
    private boolean isCompleted;

    public TaskRequest() {
    }

    public TaskRequest(String title, String descriprion, boolean isCompleted) {
        this.title = title;
        this.descriprion = descriprion;
        this.isCompleted = isCompleted;
    }
     public String getTitle() {
        return title;
     }

     public String getDescriprion() {
        return descriprion;
     }

     public boolean getIsCompleted() {
        return isCompleted;
     }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescriprion(String descriprion) {
        this.descriprion = descriprion;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
