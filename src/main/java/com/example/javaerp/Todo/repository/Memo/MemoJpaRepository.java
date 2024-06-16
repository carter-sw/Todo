package com.example.javaerp.Todo.repository.Memo;

import com.example.javaerp.Todo.repository.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoJpaRepository extends JpaRepository<Memo,Integer> {
    List<Memo> findByTodo_Id(Integer todoId);
}
