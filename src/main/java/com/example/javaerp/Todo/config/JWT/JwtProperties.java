package com.example.javaerp.Todo.config.JWT;

public interface JwtProperties {


    long EXPIRATION_TIME = 1000L * 60 * 60; // 1시간 // Token 만료(10분 설정*5)
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}