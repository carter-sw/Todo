package com.example.javaerp.Todo.repository.Memo;

import com.example.javaerp.Todo.repository.Todo.Todo;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "memos")
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;
}

