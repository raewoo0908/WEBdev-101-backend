package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //스프링 빈으로 등록
public class WebMvcConfig implements WebMvcConfigurer {
    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")  //모든 경로에 대해
                .allowedOrigins("http://localhost:3000") //Origin이 http://localhost:3000에 대해
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") //"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS" 메서드를 허용한다.
                .allowedHeaders("*") //모든 Header와
                .allowCredentials(true) //모든 인증에 대한 정보도 허용한다.
                .maxAge(MAX_AGE_SECS);
    }
}
