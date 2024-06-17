package com.example.demo;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication //이 클래스가 스프링부트를 설정하는 클래스입니다. 이 클래스가 담긴 패키지를 베이스패키지로 간주합니다.
@Builder
@RequiredArgsConstructor
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		@NonNull
		String id;
	}

}
