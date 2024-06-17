package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Slf4j
@Configuration
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.cors(Customizer.withDefaults()) //WebMvcConfig에서 이미 설정했으므로 기본 cors 설정.
                .csrf(AbstractHttpConfigurer::disable) //csrf는 현재 사용하지 않으므로 disable
                .httpBasic(HttpBasicConfigurer::disable) //token을 사용하므로 basic 인증 disable
                .sessionManagement(configure -> configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //session 기반이 아님을 선언
                .authorizeHttpRequests((auths) -> auths
                        .requestMatchers("/").permitAll() // /와 /auth/**경로는 인증 안 해도 됨
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()); //그 외의 모든 경로는 인증해야 함.
        //Filter 등록.
        //매 요청마다 CorsFilter 실행한 후에 jwtAuthenticationFilter를 실행한다.
        http.addFilterAfter(
                jwtAuthenticationFilter,
                CorsFilter.class
        );

        return http.build();
    }
}
