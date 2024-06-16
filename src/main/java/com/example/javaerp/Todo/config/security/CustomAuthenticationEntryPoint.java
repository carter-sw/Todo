package com.example.javaerp.Todo.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        response.sendRedirect("/v1/api/login");
        // commence 메소드는 인증 과정에서 예외가 발생했을 때 실행
        // 사용자를 "/v1/api" 경로로 리다이렉트 이는 사용자가 인증되지 않았을 때 보여줄 페이지 또는 로그인 페이지로 이동시키는 용도로 사용
    }
}