package com.line4thon.fin4u.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RecommentClientConfig {
    @Bean("recommendClient")
    public WebClient recommendClient(){
        return WebClient.builder()
                //컨테이너명으로 수정
                .baseUrl("http://127.0.0.1:8000")
                .build();
    }
}
