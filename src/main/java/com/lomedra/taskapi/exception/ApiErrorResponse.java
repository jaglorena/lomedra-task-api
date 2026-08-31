package com.lomedra.taskapi.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ApiErrorResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String message;
    private final Map<String, String> errors;

    public ApiErrorResponse(
            LocalDateTime timestamp,
            int status,
            String message,
            Map<String, String>errors){
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
