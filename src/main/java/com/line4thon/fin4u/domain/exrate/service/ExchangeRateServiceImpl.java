package com.line4thon.fin4u.domain.exrate.service;

import com.line4thon.fin4u.domain.exrate.KoreanBank.Country;
import com.line4thon.fin4u.domain.exrate.web.dto.ExchangeRateRes;
import com.line4thon.fin4u.domain.exrate.KoreanBank.KoreanBankApiProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final KoreanBankApiProvider provider;

    @Override
    public List<ExchangeRateRes> getExchangeRateGraphData() {
        ExchangeRateRes usdExchangeRate = provider
                .callExchangeRate(Country.USD_CODE.getCurrencyCode()
                );

        ExchangeRateRes cnyExchangeRate = provider
                .callExchangeRate(
                        Country.CNY_CODE.getCurrencyCode()
                );

        ExchangeRateRes vndExchangeRate = provider
                .callExchangeRate(
                        Country.VND_CODE.getCurrencyCode()
                );
        return List.of(usdExchangeRate, cnyExchangeRate, vndExchangeRate);
    }

    @Override
    public Object getToastMessage() {
        return null;
    }
}
