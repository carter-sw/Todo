package com.example.javaerp.Todo.repository.Notification;

import com.example.javaerp.Todo.repository.Schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationJpaRepository extends JpaRepository<Notification,Integer> {
    List<Notification> findTop5ByOrderByCreatedAtDesc();
}
