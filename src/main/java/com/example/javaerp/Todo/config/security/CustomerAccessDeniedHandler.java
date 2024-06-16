package com.example.javaerp.Todo.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Slf4j
@Component
public class CustomerAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 사용자가 접근 권한이 없는 자원에 접근했을 때 실행되는 메소드
        // 접근 거부 예외의 스택 트레이스를 콘솔에 출력
        accessDeniedException.printStackTrace();
        // 사용자를 "/exceptions/access-denied" 경로로 리다이렉트합니다.
        // 이는 사용자가 접근 권한이 없을 때 보여줄 페이지로 이동시키는 용도로 사용될 수 있습니다.
        response.sendRedirect("/v1/api/login");
    }



}
