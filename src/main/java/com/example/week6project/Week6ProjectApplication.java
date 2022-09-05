package com.example.week6project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableScheduling
@SpringBootApplication
public class Week6ProjectApplication {
    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";
    public static final String ALLOWED_HEADERS = "Origin, Content-Type, X-Auth-Token";
    public static void main(String[] args) {
        SpringApplication.run(Week6ProjectApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
                        .allowedHeaders(ALLOWED_HEADERS.split(","))
                        .exposedHeaders("Authorization", "RefreshToken")
                        .maxAge(3600);
            }
        };
    }
}
