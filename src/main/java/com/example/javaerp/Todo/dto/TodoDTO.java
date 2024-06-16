package com.example.javaerp.Todo.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoDTO {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
}
