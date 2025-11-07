package com.line4thon.fin4u;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// 애플리케이션 전체 컨텍스트가 정상적으로 로딩되는지만 확인하는 기본 부트스트랩 테스트
// CI 환경에서 환경 변수 세팅에 영향을 받아 불필요하게 실패를 유발 가능성이 존재
// 실제 기능 검증을 수행하지 않기 때문에, CI에서 테스트를 진행하지 않을 것 같아 비활성화했습니다!
@Disabled("전체 컨텍스트 로딩 테스트는 CI에서 필요하지 않음")
@SpringBootTest
class Fin4uApplicationTests {

	@Test
	void contextLoads() {
	}

}
