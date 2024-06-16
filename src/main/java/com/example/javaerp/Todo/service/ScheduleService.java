package com.example.javaerp.Todo.service;

import com.example.javaerp.Todo.dto.ScheduleDTO;
import com.example.javaerp.Todo.repository.Schedule.Schedule;
import com.example.javaerp.Todo.repository.Schedule.ScheduleJpaRepository;
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
public class ScheduleService {
    private final ScheduleJpaRepository scheduleJpaRepository;

    public ScheduleDTO createSchedule(ScheduleDTO scheduleDto) {
        Schedule schedule = convertDtoToEntity(scheduleDto);
        Schedule savedSchedule = scheduleJpaRepository.save(schedule);
        return convertEntityToDto(savedSchedule);
    }

    public ScheduleDTO updateSchedule(Integer id, ScheduleDTO scheduleDto) {
        Schedule schedule = scheduleJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(GlobalExceptionMessage.Schedule_not_found));
        schedule.setTitle(scheduleDto.getTitle());
        schedule.setDescription(scheduleDto.getDescription());
        schedule.setStartDateTime(scheduleDto.getStartDateTime());
        schedule.setEndDateTime(scheduleDto.getEndDateTime());
        Schedule updatedSchedule = scheduleJpaRepository.save(schedule);
        return convertEntityToDto(updatedSchedule);
    }

    public void deleteSchedule(Integer scheduleId) {
        Schedule schedule = scheduleJpaRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException(GlobalExceptionMessage.Schedule_not_found));
        scheduleJpaRepository.delete(schedule);
    }

    public List<ScheduleDTO> getSchedulesByUser(Integer userId) {
        List<Schedule> schedules = scheduleJpaRepository.findByUserId(userId);
        return schedules.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getSchedulesByUserAndDateRange(Integer userId, LocalDateTime start, LocalDateTime end) {
        List<Schedule> schedules = scheduleJpaRepository.findByUserIdAndStartDateTimeBetween(userId, start, end);
        return schedules.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }
    private Schedule convertDtoToEntity(ScheduleDTO scheduleDto) {
        return Schedule.builder()
                .id(scheduleDto.getId())
                .title(scheduleDto.getTitle())
                .description(scheduleDto.getDescription())
                .startDateTime(scheduleDto.getStartDateTime())
                .endDateTime(scheduleDto.getEndDateTime())
                .build();
    }

    private ScheduleDTO convertEntityToDto(Schedule schedule) {
        return ScheduleDTO.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .startDateTime(schedule.getStartDateTime())
                .endDateTime(schedule.getEndDateTime())
                .build();
    }
}
