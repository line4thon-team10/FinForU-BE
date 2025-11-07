package com.line4thon.fin4u.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory connectionFactory
    ) {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new Jdk8Module())                           // Optional, Stream 등 jdk 8 이상부터 지원하는 모듈 임포트
                .registerModule(new ParameterNamesModule())                 // record 생성자 및 속성 매핑 지원
                .registerModule(new JavaTimeModule())                       // LocalDate 타입 직렬화
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);   // 날짜를 문자열로 직렬화

        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                // 캐시 유효기간 12시간으로 설정
                .entryTtl(Duration.ofHours(12))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))

                // DTO가 record이므로, GenericJackson2JsonRedisSerializer에서 레코드를 직렬화에 실패하지 않게 추가
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer(objectMapper)
                ));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }
}
