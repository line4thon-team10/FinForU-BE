package com.line4thon.fin4u.domain.exrate;

import com.line4thon.fin4u.domain.exrate.KoreanBank.Country;
import com.line4thon.fin4u.domain.exrate.KoreanBank.KoreanBankApiProvider;
import com.line4thon.fin4u.domain.exrate.service.ExchangeRateServiceImpl;
import com.line4thon.fin4u.domain.exrate.web.dto.ExchangeRateRes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.anthropic.AnthropicChatModel;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ExRateTest {

    @InjectMocks
    private ExchangeRateServiceImpl service;

    @Mock
    private KoreanBankApiProvider koreanBankProvider;

    @Mock
    private AnthropicChatModel claude;

    @DisplayName("환율 정보 조회 서비스 로직 테스트")
    @Test
    void getExchangeRateData() {
        given(koreanBankProvider
                .callExchangeRate(
                        Country.USD_CODE.getCurrencyCode()))
                .willReturn(new ExchangeRateRes(
                        new ExchangeRateRes.ExchangeRateData(null,null,null, List.of(new ExchangeRateRes.Exchange(LocalDate.now(), 1.0)) ,null), "")
                        .ExchangeRateData());

        given(koreanBankProvider
                .callExchangeRate(
                        Country.CNY_CODE.getCurrencyCode()))
                .willReturn(new ExchangeRateRes(
                        new ExchangeRateRes.ExchangeRateData(null,null,null, List.of(new ExchangeRateRes.Exchange(LocalDate.now(), 1.0)) ,null), "")
                        .ExchangeRateData());

        given(koreanBankProvider
                .callExchangeRate(
                        Country.VND_CODE.getCurrencyCode()))
                .willReturn(new ExchangeRateRes(
                        new ExchangeRateRes.ExchangeRateData(null,null,null, List.of(new ExchangeRateRes.Exchange(LocalDate.now(), 1.0)) ,null), "")
                        .ExchangeRateData());

        List<ExchangeRateRes> response = service.getExchangeRateGraphData();

        assertThat(response).isNotNull();
        assertThat(response.stream()
                .filter(data -> data.ExchangeRateData() != null)
                .toList())
                .isNotEmpty();

        verify(koreanBankProvider).callExchangeRate(Country.USD_CODE.getCurrencyCode());
        verify(koreanBankProvider).callExchangeRate(Country.CNY_CODE.getCurrencyCode());
        verify(koreanBankProvider).callExchangeRate(Country.VND_CODE.getCurrencyCode());
    }
}
