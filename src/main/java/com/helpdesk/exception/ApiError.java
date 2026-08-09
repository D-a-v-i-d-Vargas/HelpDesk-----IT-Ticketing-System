package com.helpdesk.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ApiError {

    private Integer status;
    private LocalDateTime errorTime;
    private String error;
    private Map<String, String> errors;

    public ApiError(Integer status, LocalDateTime errorTime, String error, Map<String, String> errors) {
        this.status = status;
        this.errorTime = errorTime;
        this.error = error;
        this.errors = errors;
    }

    public ApiError(Integer status, LocalDateTime errorTime, String error) {
        this.status = status;
        this.errorTime = errorTime;
        this.error = error;
    }
}
