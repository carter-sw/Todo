package com.example.javaerp.Todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUp {
    private String name; // NOT NUL
    private String email; // NOT NUL
    private String password; // NOT NUL
    private String roles; // NOT NULL 기본 USER_GUEST

}
