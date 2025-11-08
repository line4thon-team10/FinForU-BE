package com.line4thon.fin4u.domain.exrate;

import com.line4thon.fin4u.domain.exrate.KoreanBank.Country;
import com.line4thon.fin4u.domain.exrate.KoreanBank.KoreanBankApiProvider;
import com.line4thon.fin4u.domain.exrate.entity.ExchangeFee;
import com.line4thon.fin4u.domain.exrate.service.ExchangeRateServiceImpl;
import com.line4thon.fin4u.domain.exrate.web.dto.ExchangeRateRes;
import com.line4thon.fin4u.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.anthropic.AnthropicChatModel;

import java.time.LocalDate;
import java.util.ArrayList;
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
                        new ExchangeRateRes.ExchangeRateData(Country.USD_CODE.getCurrencyType(),0.0,0.0, List.of(new ExchangeRateRes.Exchange(LocalDate.now(), 1.0)), getFees(Country.USD_CODE.getCurrencyCode())), "usd")
                        .ExchangeRateData());

        given(koreanBankProvider
                .callExchangeRate(
                        Country.CNY_CODE.getCurrencyCode()))
                .willReturn(new ExchangeRateRes(
                        new ExchangeRateRes.ExchangeRateData(Country.CNY_CODE.getCurrencyType(),0.0,0.0, List.of(new ExchangeRateRes.Exchange(LocalDate.now(), 1.0)) ,getFees(Country.CNY_CODE.getCurrencyCode())), "cny")
                        .ExchangeRateData());

        given(koreanBankProvider
                .callExchangeRate(
                        Country.VND_CODE.getCurrencyCode()))
                .willReturn(new ExchangeRateRes(
                        new ExchangeRateRes.ExchangeRateData(Country.VND_CODE.getCurrencyType(),0.0,0.0, List.of(new ExchangeRateRes.Exchange(LocalDate.now(), 1.0)) ,getFees(Country.VND_CODE.getCurrencyCode())), "vnd")
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

    private List<ExchangeRateRes.Fee> getFees(String currencyCode) {
        List<ExchangeRateRes.Fee> eachBankFee = new ArrayList<>();
        switch(Country.getCurrencyType(currencyCode)) {
            case "USD":
                for(ExchangeFee fee : ExchangeFee.values()) {
                    eachBankFee.add(new ExchangeRateRes.Fee(
                            fee.getBank(),
                            fee.getUsdFee()
                    ));
                }
                break;
            case "CNY":
                for(ExchangeFee fee : ExchangeFee.values()) {
                    eachBankFee.add(new ExchangeRateRes.Fee(
                            fee.getBank(),
                            fee.getCnyFee()
                    ));
                }
                break;
            case "VND":
                for(ExchangeFee fee : ExchangeFee.values()) {
                    eachBankFee.add(new ExchangeRateRes.Fee(
                            fee.getBank(),
                            fee.getVndFee()
                    ));
                }
                break;
            default:
                throw new IllegalStateException("Unknown currency code " + currencyCode);
        }
        return eachBankFee;
    }
}
