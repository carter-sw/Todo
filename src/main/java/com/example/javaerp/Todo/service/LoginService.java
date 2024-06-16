package com.example.javaerp.Todo.service;

import com.example.javaerp.Todo.config.JWT.JwtTokenProvider;
import com.example.javaerp.Todo.dto.Login;
import com.example.javaerp.Todo.dto.SignUp;
import com.example.javaerp.Todo.repository.Member.Member;
import com.example.javaerp.Todo.repository.Member.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberJpaRepository memberJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional(transactionManager = "tmJpa")
    public boolean signUp(SignUp signUpRequest) {
        String name = signUpRequest.getName();
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();
        String role = signUpRequest.getRoles(); // 기본 역할은 "USER"

        // 비밀번호 생성 규칙 확인
        if (!isPasswordValid(password)) {
            log.info("비밀번호는 영문자, 숫자, 특수문자를 조합하여 8글자에서 20글자 사이로 설정해주세요.");
            return false; // 비밀번호 규칙 미준수 시 회원가입 실패
        }

        // 이메일 중복 확인
        if (isEmailAlreadyRegistered(email)) {
            return false; // 이미 등록된 이메일이면 회원가입 실패
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);
        // 이메일(아이디) 해시화
        String hashedEmail = hashEmail(email);

        // 회원 정보 저장
        memberJpaRepository.save(Member.builder()
                .name(name)
                .email(hashedEmail)
                .password(encodedPassword)
                .role(role)
                .build());

        return true; // 회원가입 성공
    }

    private String hashEmail(String email) {
        return DigestUtils.sha256Hex(email);
    }

    private boolean isPasswordValid(String password) {
        // 정규 표현식을 사용하여 비밀번호가 영문자와 숫자의 조합으로 8자 이상 20자 이하인지 확인
        String regex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";
        return password.matches(regex);
    }

    private boolean isEmailAlreadyRegistered(String email) {
        Optional<Member> existingMember = memberJpaRepository.findByEmail(email);
        return existingMember.isPresent();
    }

    public String login(Login loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // 인증 시도
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT 토큰 생성
        return jwtTokenProvider.createToken(email);
    }

    public String createToken(String email) {
        return jwtTokenProvider.createToken(email);
    }

    public Optional<Member> getMemberById(Integer id) {
        return memberJpaRepository.findById(id);
    }

    public Optional<Member> getMemberByEmail(String email) {
        return memberJpaRepository.findByEmail(email);
    }

    @Transactional(transactionManager = "tmJpa")
    public boolean updateMember(Integer id, String name, String password) {
        Optional<Member> optionalMember = memberJpaRepository.findById(id);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setName(name);
            member.setPassword(passwordEncoder.encode(password));
            memberJpaRepository.save(member);
            return true;
        }
        return false;
    }

    @Transactional(transactionManager = "tmJpa")
    public boolean deleteMemberById(Integer id) {
        Optional<Member> optionalMember = memberJpaRepository.findById(id);
        if (optionalMember.isPresent()) {
            memberJpaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(transactionManager = "tmJpa")
    public boolean deleteMemberByEmail(String email) {
        Optional<Member> optionalMember = memberJpaRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            memberJpaRepository.deleteByEmail(email);
            return true;
        }
        return false;
    }
}
