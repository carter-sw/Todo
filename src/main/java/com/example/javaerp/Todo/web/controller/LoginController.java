package com.example.javaerp.Todo.web.controller;

import com.example.javaerp.Todo.config.JWT.JwtProperties;
import com.example.javaerp.Todo.config.JWT.JwtTokenProvider;
import com.example.javaerp.Todo.config.security.CustomLogoutHandler;
import com.example.javaerp.Todo.dto.Login;
import com.example.javaerp.Todo.dto.SignUp;
import com.example.javaerp.Todo.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomLogoutHandler customLogoutHandler;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login loginRequest, HttpServletResponse httpServletResponse) {
        // 로그인 시도
        String userEmail = loginService.login(loginRequest);

        // 토큰 생성 및 설정
        String token = jwtTokenProvider.createToken(userEmail);
        log.info("jwt 토큰 :" + token);
        httpServletResponse.setHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);

        // 로그인 성공 시 토큰 반환
        return ResponseEntity.ok(token);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUp signUpRequest) {
        boolean isSignUpSuccess = loginService.signUp(signUpRequest);
        if (isSignUpSuccess) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        // 현재 사용자 로그아웃 처리
        customLogoutHandler.logout(request, response, null);
        return ResponseEntity.ok().build();
    }
}
