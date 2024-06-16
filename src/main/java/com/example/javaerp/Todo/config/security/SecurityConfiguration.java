package com.example.javaerp.Todo.config.security;


import com.example.javaerp.Todo.config.JWT.JwtAuthenticationFilter;
import com.example.javaerp.Todo.config.JWT.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationConfiguration authenticationConfiguration;


    // SecurityFilterChain 빈을 등록하여 HTTP 요청에 대한 보안 필터 체인을 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable()); // CSRF 보호 기능 비활성화
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));  // session 사용 안함 (stateless)
        http
                .formLogin(f -> f.disable()) // 폼태그(html)를 사용해서 로그인 사용안함
                .httpBasic(h -> h.disable())
                // 클라이언트가 서버에 요청을 보낼 때 사용자 이름과 비밀번호를 인증하는 간단한 방법
                // 클라이언트가 요청을 보낼 때, 인증 정보는 요청 헤더에 Base64로 인코딩되어 포함. 서버는 이 정보를 확인하여 클라이언트의 요청을 처리하거나 거부.
                // 이 방법은 간단하고 쉽게 구현할 수 있지만, 보안 수준이 낮고 인증 정보가 평문으로 전송되기 때문에 보안에 취약함.
                // Http Basic 인증을 사용하지 않도록 Spring Security 구성을 설정
                .authorizeHttpRequests(authorize -> // 요청에 대한 접근 권한을 설정
                        authorize.requestMatchers("/v1/api/logout").authenticated() // 로그아웃은 인증된 사용자에게만 허용
                                .requestMatchers("/v1/api/guest/**").hasAnyRole("GUEST", "HOST")
                                .requestMatchers("/v1/api/host/**").hasRole("HOST")
                                .requestMatchers("/v1/api/sign", "/v1/api/login", "/v2/api-docs",
                                        "/configuration/ui", "/swagger-resources/**", "/configuration/security",
                                        "/swagger-ui/**", "/webjars/**", "/swagger/**").permitAll() // 회원가입, 로그인 경로는 모두에게 허용 Swagger 문서 관련 경로는 모두에게 허용
                                .anyRequest().permitAll()) // 그 외 모든 요청은 인증을 필요로 함
                .logout(logout -> logout.logoutUrl("/v1/api/logout") // 로그아웃 설정 ,  로그아웃 처리 URL
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                        .logoutSuccessHandler(((request, response, authentication) -> {
                            // 로그아웃 성공 시 POST 요청을 보냄
                            response.setStatus(HttpServletResponse.SC_OK); //서버 응답의 상태 코드를 설정.  200 = OK
                            response.setContentType("application/json"); // 응답의 컨텐츠 타입을 설정  JSON 형식
                            response.setCharacterEncoding("UTF-8"); // 응답의 문자 인코딩 설정. UTF-8 사용
                            response.getWriter().write("{\"message\": \"로그아웃이 완료되었습니다.\"}");
                        }))

                        .permitAll())
                .exceptionHandling(e -> e.authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증 실패 시 처리를 위한 진입점 설정
                        .accessDeniedHandler(new CustomerAccessDeniedHandler())                // 예외 처리 설정
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // JWT 인증 필터 추가


        return http.build(); // HttpSecurity 설정을 기반으로 SecurityFilterChain 객체 생성 및 반환
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // AuthenticationManager를 반환
    }

}
