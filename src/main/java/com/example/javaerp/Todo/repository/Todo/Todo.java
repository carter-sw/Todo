package com.example.javaerp.Todo.repository.Todo;

import com.example.javaerp.Todo.repository.Member.Member;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "todos")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "completed")
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;
}
