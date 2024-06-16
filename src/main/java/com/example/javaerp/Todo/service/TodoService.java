package com.example.javaerp.Todo.service;

import com.example.javaerp.Todo.dto.TodoDTO;
import com.example.javaerp.Todo.repository.Todo.Todo;
import com.example.javaerp.Todo.repository.Todo.TodoJpaRepository;
import com.example.javaerp.Todo.web.error.GlobalExceptionMessage;
import com.example.javaerp.Todo.web.error.TodoNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoJpaRepository todoJpaRepository;

    public List<TodoDTO> getAllTodos() {
        List<Todo> todos = todoJpaRepository.findAll();
        return todos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TodoDTO getTodoById(Integer id) {
        Todo todo = todoJpaRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(GlobalExceptionMessage.TodoNotFoundExceptionMessage + id));
        return toDTO(todo);
    }

    public TodoDTO createTodo(TodoDTO todoDTO) {
        Todo todo = Todo.builder()
                .title(todoDTO.getTitle())
                .description(todoDTO.getDescription())
                .completed(todoDTO.isCompleted())
                .build();
        Todo savedTodo = todoJpaRepository.save(todo);
        return toDTO(savedTodo);
    }

    public TodoDTO updateTodo(Integer id, TodoDTO updatedTodoDTO) {
        Todo existingTodo = todoJpaRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(GlobalExceptionMessage.TodoNotFoundExceptionMessage + id));
        Todo updatedTodo = existingTodo.builder()
                .title(updatedTodoDTO.getTitle())
                .description(updatedTodoDTO.getDescription())
                .completed(updatedTodoDTO.isCompleted())
                .build();
        Todo savedTodo = todoJpaRepository.save(updatedTodo);
        return toDTO(savedTodo);
    }

    public void deleteTodo(Integer id) {
        Todo existingTodo = todoJpaRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(GlobalExceptionMessage.TodoNotFoundExceptionMessage + id));
        todoJpaRepository.delete(existingTodo);
    }

    public List<TodoDTO> getTodos(String title, String description, Boolean completed) {
        List<Todo> todos = todoJpaRepository.findAll();

        if (title != null) {
            todos = todos.stream()
                    .filter(todo -> todo.getTitle().contains(title))
                    .collect(Collectors.toList());
        }

        if (description != null) {
            todos = todos.stream()
                    .filter(todo -> todo.getDescription().contains(description))
                    .collect(Collectors.toList());
        }

        if (completed != null) {
            todos = todos.stream()
                    .filter(todo -> todo.isCompleted() == completed)
                    .collect(Collectors.toList());
        }

        return todos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private TodoDTO toDTO(Todo todo) {
        return TodoDTO.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .completed(todo.isCompleted())
                .build();
    }
    private Todo toEntity(TodoDTO todoDTO) {
        return Todo.builder()
                .id(todoDTO.getId())
                .title(todoDTO.getTitle())
                .description(todoDTO.getDescription())
                .completed(todoDTO.isCompleted())
                .build();
    }

}

