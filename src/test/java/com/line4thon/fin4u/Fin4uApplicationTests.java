package com.line4thon.fin4u;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

// 해당 부분은 DB 없이 스프링 빈들이 정상적으로 기동되는지(컨텍스트 로딩)만 확인
@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
class Fin4uApplicationTests {

    // 테스트에서 외부 API 호출을 막고 스프링 컨텍스트 로딩을 위해 ChatClient를 가짜(Mock) 빈으로 대체
    @Qualifier("mcpClient")
    ChatClient chatClient;

	@Test
	void contextLoads() {
	}

}
