package com.hotel.erp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                                .allowedOrigins("http://localhost:3000", "http://localhost:8088",
                                                "http://localhost:8080", "http://127.0.0.1:3000")
                                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
                                .allowedHeaders("*")
                                .allowCredentials(false)
                                .maxAge(3600);
        }

        @Override
        public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
                // Serve static resources
                registry.addResourceHandler("/static/**")
                                .addResourceLocations("classpath:/static/");

                // Handle favicon and other static files
                registry.addResourceHandler("/favicon.ico")
                                .addResourceLocations("classpath:/static/favicon.ico");

                registry.addResourceHandler("/logo192.png")
                                .addResourceLocations("classpath:/static/logo192.png");
        }
}