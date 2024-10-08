package com.example.back_end.util.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3000")  // 허용할 프론트엔드 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")  // 모든 요청 헤더 허용
                .exposedHeaders("Authorization")  // 클라이언트가 헤더에 접근할 수 있도록 노출
                .allowCredentials(true);  // 인증 정보 허용 (쿠키 등)
    }
}
