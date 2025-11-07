package com.line4thon.fin4u.domain.exrate.service;

import com.line4thon.fin4u.domain.exrate.KoreanBank.Country;
import com.line4thon.fin4u.domain.exrate.web.dto.ExchangeRateRes;
import com.line4thon.fin4u.domain.exrate.KoreanBank.KoreanBankApiProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final KoreanBankApiProvider provider;
    private final AnthropicChatModel claude;

    @Override
    @Cacheable(value = "exchangeRates")
    public List<ExchangeRateRes> getExchangeRateGraphData() {
        ExchangeRateRes.ExchangeRateData usdExchangeRateData = provider
                .callExchangeRate(Country.USD_CODE.getCurrencyCode()
                );
        List<ExchangeRateRes.Exchange> usdWeekData = usdExchangeRateData.priceGraphData().stream()
                .filter(data -> !data.date().isBefore(LocalDate.now().minusDays(7)))
                .toList();
        ExchangeRateRes usdExchangeRate = new ExchangeRateRes(usdExchangeRateData, getToastMessage(usdWeekData));

        ExchangeRateRes.ExchangeRateData cnyExchangeRateData = provider
                .callExchangeRate(
                        Country.CNY_CODE.getCurrencyCode()
                );
        List<ExchangeRateRes.Exchange> cnyWeekData = cnyExchangeRateData.priceGraphData().stream()
                .filter(data -> !data.date().isBefore(LocalDate.now().minusDays(7)))
                .toList();
        ExchangeRateRes cnyExchangeRate = new ExchangeRateRes(cnyExchangeRateData, getToastMessage(cnyWeekData));

        ExchangeRateRes.ExchangeRateData vndExchangeRateData = provider
                .callExchangeRate(
                        Country.VND_CODE.getCurrencyCode()
                );
        List<ExchangeRateRes.Exchange> vndWeekData = vndExchangeRateData.priceGraphData().stream()
                .filter(data -> !data.date().isBefore(LocalDate.now().minusDays(7)))
                .toList();
        ExchangeRateRes vndExchangeRate = new ExchangeRateRes(vndExchangeRateData, getToastMessage(vndWeekData));
        return List.of(usdExchangeRate, cnyExchangeRate, vndExchangeRate);
    }

    private String getToastMessage(List<ExchangeRateRes.Exchange> list) {
        return claude.call(
                list.toString() + "You must tell me that Using " + "eng" + "| 이 정보들은 최근 1주일의 영업일 동안의" +
                        "단위 통화당 원화 가격입니다.(ex. 1dollar = 1400.0). " +
                        "당신은 제가 제공한 정보에 대한 분석을 제공하지 마십시오. 오직 오늘, 제가 환전했을 때" +
                        "이득인지 아닌지에 대한 정보만 필요합니다. **정보에 근거해서, 라는 말은 붙이지 마십시오.**" +
                        "당신은 제게 **현재** 원화->다른 통화로 환전했을 때, " +
                        "이득인지 아닌지 알려주어야 합니다. 다음은 예시입니다. \"It's better to transfer money today!\"" +
                        "절대 이 예시를 넘어서 더 길어지면 안 됩니다. 그리고 완전히 이 예시를 따라가지 마십시오." +
                        "제게 오늘 환전하면 좋은지 안 좋을지 알려주는 1줄짜리 toastMessage 생성을 담당하고 있습니다." +
                        "토스트 메세지는 다양하게 생성되면 좋고, 사용자에게 친숙한 어조로 말씀해주세요." +
                        "영단어로 했을 때, 최대 10단어 이상을 안 넘어가는 길이었으면 좋겠습니다." +
                        "다른 언어도 비슷한 길이로 맞추어 주세요."
        );
    }
}
