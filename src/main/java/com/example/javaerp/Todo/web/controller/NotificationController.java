package com.example.javaerp.Todo.web.controller;

import com.example.javaerp.Todo.dto.NotificationDTO;
import com.example.javaerp.Todo.dto.TodoDTO;
import com.example.javaerp.Todo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/new-todo")
    public ResponseEntity<Void> sendNewTodoNotification(@RequestBody TodoDTO newTodo) {
        notificationService.sendNewTodoNotification(newTodo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/completed-todo")
    public ResponseEntity<Void> sendCompletedTodoNotification(@RequestBody TodoDTO completedTodo) {
        notificationService.sendCompletedTodoNotification(completedTodo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getRecentNotifications() {
        List<NotificationDTO> notifications = notificationService.getRecentNotifications();
        return ResponseEntity.ok(notifications);
    }
}
