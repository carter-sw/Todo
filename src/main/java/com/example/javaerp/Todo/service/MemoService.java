package com.example.javaerp.Todo.service;

import com.example.javaerp.Todo.dto.MemoDTO;
import com.example.javaerp.Todo.repository.Memo.Memo;
import com.example.javaerp.Todo.repository.Memo.MemoJpaRepository;
import com.example.javaerp.Todo.repository.Todo.Todo;
import com.example.javaerp.Todo.repository.Todo.TodoJpaRepository;
import com.example.javaerp.Todo.web.error.GlobalExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemoService {
    private final MemoJpaRepository memoJpaRepository;
    private final TodoJpaRepository todoJpaRepository;
    public MemoDTO createMemo(MemoDTO memoDTO) {
        try {
            Todo todo = todoJpaRepository.findById(Math.toIntExact(memoDTO.getTodoId()))
                    .orElseThrow(() -> new RuntimeException(GlobalExceptionMessage.Todo_not_found + memoDTO.getTodoId()));

            Memo memo = Memo.builder()
                    .title(memoDTO.getTitle())
                    .content(memoDTO.getContent())
                    .todo(todo)
                    .build();

            Memo savedMemo = memoJpaRepository.save(memo);

            return MemoDTO.builder()
                    .id(Long.valueOf(savedMemo.getId()))
                    .title(savedMemo.getTitle())
                    .content(savedMemo.getContent())
                    .todoId(savedMemo.getTodo().getId())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(GlobalExceptionMessage.Failed_create_memo_Message + e.getMessage(), e);
        }
    }

    public List<MemoDTO> getMemosByTodo(Integer todoId) {
        try {
            List<Memo> memos = memoJpaRepository.findByTodo_Id(todoId);
            return memos.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(GlobalExceptionMessage.Failed_memos_todo_Message + e.getMessage(), e);
        }
    }

    private MemoDTO convertToDTO(Memo memo) {
        return MemoDTO.builder()
                .id(Long.valueOf(memo.getId()))
                .title(memo.getTitle())
                .content(memo.getContent())
                .todoId(memo.getTodo().getId())
                .build();
    }

}

