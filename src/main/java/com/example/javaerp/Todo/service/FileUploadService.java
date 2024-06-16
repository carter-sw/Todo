package com.example.javaerp.Todo.service;

import com.example.javaerp.Todo.repository.Member.Member;
import com.example.javaerp.Todo.repository.Member.MemberJpaRepository;
import com.example.javaerp.Todo.web.error.GlobalExceptionMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileUploadService {
    private final MemberJpaRepository memberJpaRepository;
    private final String UPLOAD_DIR = "uploads/";
    public void uploadProfileImage(Integer userId, MultipartFile file) {
        Member member = memberJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(GlobalExceptionMessage.EntityNotFoundExceptionMessage));

        String fileName = file.getOriginalFilename();
        String filePath = UPLOAD_DIR + fileName;

        try {
            File uploadedFile = new File(filePath);
            file.transferTo(uploadedFile);
            member.setProfileImagePath(filePath);
            memberJpaRepository.save(member);
        } catch (IOException e) {
            throw new RuntimeException(GlobalExceptionMessage.Fileupload_errorMessage, e);
        }
    }

    public void uploadResume(Integer userId, MultipartFile file) {
        Member member = memberJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(GlobalExceptionMessage.EntityNotFoundExceptionMessage));

        String fileName = file.getOriginalFilename();
        String filePath = UPLOAD_DIR + fileName;

        try {
            File uploadedFile = new File(filePath);
            file.transferTo(uploadedFile);
            member.setResumeFilePath(filePath);
            memberJpaRepository.save(member);
        } catch (IOException e) {
            throw new RuntimeException(GlobalExceptionMessage.Fileupload_errorMessage, e);
        }
    }
}
