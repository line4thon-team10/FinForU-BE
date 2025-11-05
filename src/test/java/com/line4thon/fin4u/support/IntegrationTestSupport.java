package com.line4thon.fin4u.support;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest // JUnit이 컨테이너 라이프사이클 자동 관리 선언 (start/stop)
@ActiveProfiles("test")
public abstract class IntegrationTestSupport {
}


