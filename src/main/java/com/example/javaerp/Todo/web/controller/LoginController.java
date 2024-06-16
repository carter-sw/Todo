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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/login")
public class LoginController {

    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomLogoutHandler customLogoutHandler;

    @GetMapping
    public String showLoginPage(Model model) {
        model.addAttribute("loginRequest", new Login());
        return "login";
    }

    @PostMapping
    public String login(@ModelAttribute("loginRequest") Login loginRequest, HttpServletResponse httpServletResponse) {
        // 로그인 시도
        String userEmail = loginService.login(loginRequest);

        // 토큰 생성 및 설정
        String token = jwtTokenProvider.createToken(userEmail);
        log.info("jwt 토큰 :" + token);
        httpServletResponse.setHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);

        // 로그인 성공 시 메인 페이지로 리다이렉트
        return "redirect:/main";
    }

    @GetMapping("/signup")
    public String showSignUpPage(Model model) {
        model.addAttribute("signUpRequest", new SignUp());
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute("signUpRequest") SignUp signUpRequest, Model model) {
        boolean isSignUpSuccess = loginService.signUp(signUpRequest);
        if (isSignUpSuccess) {
            return "redirect:/login";
        } else {
            model.addAttribute("errorMessage", "회원가입에 실패했습니다.");
            return "signup";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 현재 사용자 로그아웃 처리
        customLogoutHandler.logout(request, response, null);
        return "redirect:/login";
    }
}
