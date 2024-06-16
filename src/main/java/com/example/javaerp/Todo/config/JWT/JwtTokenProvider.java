package com.example.javaerp.Todo.config.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret-key-source}") // application.yml 에서 jwt.secret-key-source 값을 가져옴
    private String secretKeySource;
    private String secretKey;

    private final UserDetailsService userDetailsService; // 사용자 정보를 로드하는 서비스

    @PostConstruct // 빈 초기화 후 실행될 메서드
    public void setUp(){
        secretKey = Base64.getEncoder()
                .encodeToString(secretKeySource.getBytes()); // 비밀키를 Base64로 인코딩
    }



    // HTTP 요청에서 토큰을 가져오는 메서드
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtProperties.HEADER_STRING);
        if (bearerToken != null && bearerToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
            return bearerToken.substring(7); // "Bearer " 무시
        }
        return null;
    }

    public String createToken(String email) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(email) // 토큰의 주제(subject)로 이메일을 설정
                .setIssuedAt(now) // 토큰 발행 시간
                .setExpiration(new Date(now.getTime() + JwtProperties.EXPIRATION_TIME)) // 토큰 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey) // HS256 알고리즘과 비밀키를 사용하여 서명
                .compact(); // JWT 생성
    }

    // JWT 토큰의 유효성을 검증하는 메소드
    public boolean validateToken(String jwtToken) {
        try {
            // JWT 토큰을 파싱해서 Claims 객체를 얻어옴
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey) // 서명 키 설정
                    .parseClaimsJws(jwtToken)// 토큰 파싱
                    .getBody(); // Payload 부분(클레임)을 가져옴

            Date experationDate = claims.getExpiration(); // 토큰 만료 시간
            Date now = new Date(); // 현재 시간
            // 토큰의 만료 시간이 현재 시간보다 이전이거나 동일하면 만료
            return experationDate != null && experationDate.after(now);
        } catch (ExpiredJwtException e) { // 만료시 예외 반환
            return false;
        }
    }

    // 주어진 JWT 토큰에서 사용자의 이메일을 추출하여 UserDetails를 로드하고, 인증 객체를 반환
    public Authentication getAuthentication(String jwtToken) {
        String userEmail = getUserEmail(jwtToken);  // 주어진 JWT 토큰에서 사용자 이메일 추출
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail); // 추출한 이메일을 사용하여 UserDetails를 로드
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        // UserDetails를 사용하여 인증 객체 생성
        // 인증에는 UserDetails와 빈 문자열(비밀번호 필드가 없는 경우) 그리고 해당 사용자의 권한이 포함.
    }

    // 주어진 JWT 토큰에서 사용자 이메일을 추출하는 메서드
    private String getUserEmail(String jwtToken) {
        // JWT 토큰을 파싱하여 Claims 객체를 얻어옴
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody();
        return claims.getSubject(); // Claims에서 subject를 추출하여 사용자의 이메일을 반환
    }
}
