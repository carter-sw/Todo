package com.example.javaerp.Todo.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemoDTO {
    private Long id;
    private String title;
    private String content;
    private Long todoId;
}
