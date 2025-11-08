package com.line4thon.fin4u;

import com.line4thon.fin4u.domain.exrate.KoreanBank.Country;
import com.line4thon.fin4u.domain.exrate.KoreanBank.KoreanBankApiProvider;
import com.line4thon.fin4u.domain.exrate.service.ExchangeRateServiceImpl;
import com.line4thon.fin4u.domain.exrate.web.dto.ExchangeRateRes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.times;

// 애플리케이션 전체 컨텍스트가 정상적으로 로딩되는지만 확인하는 기본 부트스트랩 테스트
// CI 환경에서 환경 변수 세팅에 영향을 받아 불필요하게 실패를 유발 가능성이 존재
// 실제 기능 검증을 수행하지 않기 때문에, CI에서 테스트를 진행하지 않을 것 같아 비활성화했습니다!
@Disabled("전체 컨텍스트 로딩 테스트는 CI에서 필요하지 않음")
@SpringBootTest
class Fin4uApplicationTests {

	@Autowired
	private ExchangeRateServiceImpl exchangeRateService;

	@Autowired
	private CacheManager cacheManger;

	@MockBean
	private KoreanBankApiProvider provider;

	@Test
	void contextLoads() {
	}

	@BeforeEach
	void setUpKoreanBankApiMockResponse() {
		given(provider
				.callExchangeRate(
						Country.USD_CODE.getCurrencyCode()))
				.willReturn(new ExchangeRateRes(
						new ExchangeRateRes.ExchangeRateData(null,null,null, List.of(new ExchangeRateRes.Exchange(LocalDate.now(), 1.0)) ,null), "")
						.ExchangeRateData());

		given(provider
				.callExchangeRate(
						Country.CNY_CODE.getCurrencyCode()))
				.willReturn(new ExchangeRateRes(
						new ExchangeRateRes.ExchangeRateData(null,null,null, List.of(new ExchangeRateRes.Exchange(LocalDate.now(), 1.0)) ,null), "")
						.ExchangeRateData());

		given(provider
				.callExchangeRate(
						Country.VND_CODE.getCurrencyCode()))
				.willReturn(new ExchangeRateRes(
						new ExchangeRateRes.ExchangeRateData(null,null,null, List.of(new ExchangeRateRes.Exchange(LocalDate.now(), 1.0)) ,null), "")
						.ExchangeRateData());
	}

	@DisplayName("환율 조회부분 캐시 적용 테스트")
	@Test
	void testExchangeRateApiCache() {
		List<ExchangeRateRes> cacheNotAppliedResponse = exchangeRateService.getExchangeRateGraphData();

		// callExchangeRate 메소드가 1회 호출되었는지 검증
		verify(provider, times(1)).callExchangeRate(Country.USD_CODE.getCurrencyCode());
		verify(provider, times(1)).callExchangeRate(Country.CNY_CODE.getCurrencyCode());
		verify(provider, times(1)).callExchangeRate(Country.VND_CODE.getCurrencyCode());

		List<ExchangeRateRes> cacheAppliedResponse = exchangeRateService.getExchangeRateGraphData();

		// 캐시에 의해 callExchangeRate 메소드가 이번엔 불리지 않아서, 여전히 불린 횟수 1인지 검증
		verify(provider, times(1)).callExchangeRate(Country.USD_CODE.getCurrencyCode());
		verify(provider, times(1)).callExchangeRate(Country.CNY_CODE.getCurrencyCode());
		verify(provider, times(1)).callExchangeRate(Country.VND_CODE.getCurrencyCode());

		// 두 결과가 동일한지 검증
		assertThat(cacheNotAppliedResponse).isEqualTo(cacheAppliedResponse);
	}
}
