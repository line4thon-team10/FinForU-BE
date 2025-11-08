package com.line4thon.fin4u.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.line4thon.fin4u.domain.exrate.KoreanBank.Country;
import com.line4thon.fin4u.domain.exrate.KoreanBank.KoreanBankApiProvider;
import com.line4thon.fin4u.domain.exrate.service.ExchangeRateService;
import com.line4thon.fin4u.domain.exrate.web.dto.ExchangeRateRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CaffeineCacheConfig {

    private final ExchangeRateService exchangeRateService;
    private final KoreanBankApiProvider provider;

    @Bean
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("exchangeRates");

        caffeineCacheManager.setCacheLoader(key -> {
            log.info("환율 조회 API의 캐시가 만료되었습니다. 캐시 갱신을 시작합니다.");
            log.info("key: {}", key);

            List<String> currencyCodes = List.of(
                    Country.USD_CODE.getCurrencyCode(),
                    Country.CNY_CODE.getCurrencyCode(),
                    Country.VND_CODE.getCurrencyCode()
            );

            List<ExchangeRateRes> list = new ArrayList<>();
            for(String code : currencyCodes) {
                ExchangeRateRes.ExchangeRateData data = provider.callExchangeRate(code);
                list.add(
                        new ExchangeRateRes(data, exchangeRateService.getToastMessage(data.priceGraphData().stream()
                        .filter(record -> !record.date().isBefore(LocalDate.now().minusDays(7)))
                        .toList()))
                );
            }
            return list;
        });

        caffeineCacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(500)
                        .refreshAfterWrite(12, TimeUnit.HOURS)
                        .recordStats()
        );

        return caffeineCacheManager;
    }
}
