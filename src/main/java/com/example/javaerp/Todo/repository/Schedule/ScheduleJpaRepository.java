package com.example.javaerp.Todo.repository.Schedule;

import com.example.javaerp.Todo.repository.Memo.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleJpaRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByUserIdAndStartDateTimeBetween(Integer userId, LocalDateTime start, LocalDateTime end);
    List<Schedule> findByUserId(Integer userId);
}

