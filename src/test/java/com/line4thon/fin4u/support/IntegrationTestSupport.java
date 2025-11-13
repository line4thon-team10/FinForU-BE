package com.line4thon.fin4u.support;

import org.mockito.Answers;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import com.google.firebase.messaging.FirebaseMessaging;

// 통합 테스트 환경 구축

@Testcontainers // 테스트시 Docker를 이용하여 db과 같은 환경 구축을 돕는 라이브러리
@SpringBootTest
public abstract class IntegrationTestSupport {

    // 통합 테스트 전체에서 ChatClient를 Mock으로 대체 (외부 연동 제거)
    @MockBean(name = "mcpClient", answer = Answers.RETURNS_DEEP_STUBS)
    protected ChatClient chatClient;

    @MockBean
    protected FirebaseMessaging firebaseMessaging;

    //테스트 실행할 때만 db를 띄움
    @Container
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    //설정값을 application.properties가 아니 Testcontainers 컨테이너 값으로 오버라이드
    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }
}

