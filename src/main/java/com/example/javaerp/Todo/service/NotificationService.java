package com.example.javaerp.Todo.service;

import com.example.javaerp.Todo.dto.NotificationDTO;
import com.example.javaerp.Todo.dto.TodoDTO;
import com.example.javaerp.Todo.repository.Notification.Notification;
import com.example.javaerp.Todo.repository.Notification.NotificationJpaRepository;
import com.example.javaerp.Todo.web.error.GlobalExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {
    private final NotificationJpaRepository notificationJpaRepository;

    public void sendNewTodoNotification(TodoDTO newTodo) {
        try {
            Notification notification = new Notification();
            notification.setContent("새로운 Todo 추가 되었습니다 : " + newTodo.getTitle());
            notification.setCreatedAt(LocalDateTime.now());
            notificationJpaRepository.save(notification);
        } catch (Exception e) {
            throw new RuntimeException(GlobalExceptionMessage.Failed_send_notification, e);
        }
    }

    public void sendCompletedTodoNotification(TodoDTO completedTodo) {
        try {
            Notification notification = new Notification();
            notification.setContent("Todo 완료 :" + completedTodo.getTitle());
            notification.setCreatedAt(LocalDateTime.now());
            notificationJpaRepository.save(notification);
        } catch (Exception e) {
            throw new RuntimeException(GlobalExceptionMessage.Failed_send_notification, e);
        }
    }

    public List<NotificationDTO> getRecentNotifications() {
        try {
            List<Notification> notifications = notificationJpaRepository.findTop5ByOrderByCreatedAtDesc();
            return notifications.stream()
                    .map(this::toNotificationDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(GlobalExceptionMessage.Failed_retrieve_notifications, e);
        }
    }

    private NotificationDTO toNotificationDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setContent(notification.getContent());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }

}
