package com.example.javaerp.Todo.repository.Todo;

import com.example.javaerp.Todo.repository.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoJpaRepository extends JpaRepository<Todo,Integer> {
}
