package com.example.interview.exception;

public class ResourceNotFoundException extends RuntimeException {
    private Long id;

    public ResourceNotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
