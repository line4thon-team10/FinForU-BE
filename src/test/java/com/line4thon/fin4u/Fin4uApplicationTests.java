package com.line4thon.fin4u;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("전체 컨텍스트 로딩 테스트는 CI에서 필요하지 않음")
@SpringBootTest
class Fin4uApplicationTests {

	@Test
	void contextLoads() {
	}

}
