package com.example.javaerp.Todo.config.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutHandler implements LogoutHandler {


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 사용자의 인증 정보를 가져옴
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        // 인증 정보가 존재하고 인증이 완료된 경우
        if (auth != null && auth.isAuthenticated()) {
            // 현재 사용자의 인증 정보를 지움.
            SecurityContextHolder.clearContext();
            // 세션을 무효화하여 로그아웃.
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            // 쿠키를 지움
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }
}