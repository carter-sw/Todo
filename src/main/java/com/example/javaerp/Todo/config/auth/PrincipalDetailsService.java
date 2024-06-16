package com.example.javaerp.Todo.config.auth;

import com.example.javaerp.Todo.repository.Member.Member;
import com.example.javaerp.Todo.repository.Member.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberJpaRepository memberJpaRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("PrincipalDetailsService의 loadUserByUsername()");
        Member membersJwtEntity = memberJpaRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을수 없습니다."));
        log.info(String.valueOf(membersJwtEntity));
        return new PrincipalDetails(membersJwtEntity);
    }
}