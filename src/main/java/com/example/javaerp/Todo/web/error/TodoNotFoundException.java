package com.example.javaerp.Todo.web.error;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(String message) {
        super(message);
    }
}