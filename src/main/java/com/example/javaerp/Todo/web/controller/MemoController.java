package com.example.javaerp.Todo.web.controller;

import com.example.javaerp.Todo.dto.MemoDTO;
import com.example.javaerp.Todo.service.MemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memos")
public class MemoController {

    private final MemoService memoService;

    @PostMapping
    public ResponseEntity<MemoDTO> createMemo(@RequestBody MemoDTO memoDTO) {
        try {
            MemoDTO createdMemo = memoService.createMemo(memoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMemo);
        } catch (Exception e) {
            log.error("메모 생성에 실패하였습니다 : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/todo/{todoId}")
    public ResponseEntity<List<MemoDTO>> getMemosByTodo(@PathVariable Integer todoId) {
        try {
            List<MemoDTO> memos = memoService.getMemosByTodo(todoId);
            return ResponseEntity.ok(memos);
        } catch (Exception e) {
            log.error("todo에 해당하는 메모를 찾을 수 없습니다 : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
