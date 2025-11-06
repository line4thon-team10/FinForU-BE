package com.line4thon.fin4u;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// 해당 부분은 DB 없이 스프링 빈들이 정상적으로 기동되는지(컨텍스트 로딩)만 확인
@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
class Fin4uApplicationTests {

	@Test
	void contextLoads() {
	}

}
