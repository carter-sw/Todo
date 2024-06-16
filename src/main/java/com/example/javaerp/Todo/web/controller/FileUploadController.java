package com.example.javaerp.Todo.web.controller;

import com.example.javaerp.Todo.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/member/{userId}/files")
@RequiredArgsConstructor
public class FileUploadController {
    private final FileUploadService fileUploadService;

    @PostMapping("/profile-image")
    public ResponseEntity<Void> uploadProfileImage(@PathVariable Integer userId, @RequestParam MultipartFile file) {
        fileUploadService.uploadProfileImage(userId, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/resume")
    public ResponseEntity<Void> uploadResume(@PathVariable Integer userId, @RequestParam MultipartFile file) {
        fileUploadService.uploadResume(userId, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}