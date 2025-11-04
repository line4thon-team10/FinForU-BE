package com.line4thon.fin4u.domain.exrate.KoreanBank;

import com.line4thon.fin4u.domain.exrate.web.dto.ExchangeData;
import com.line4thon.fin4u.domain.exrate.web.dto.ExchangeRateRes;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@Getter
public class KoreanBankApiProvider {
    private String url;

    @Value("${korean-bank.api-key}")
    private String apiKey;
    private WebClient client;

    public ExchangeRateRes callExchangeRate(String currencyCode) {
        String now = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String yesterday = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE);
        log.info("KoreanBank API Key : {}", apiKey);

        init(now, currencyCode);

        ExchangeData response = client.get()
                .uri(this.url)
                .retrieve()
                .bodyToMono(ExchangeData.class)
                .block();

        log.info("KoreanBank How many records : {}", response.StatisticSearch().row().size());

        Map<String, Double> exchangeRates = response.StatisticSearch().row().stream()
                .collect(Collectors.toMap(
                        ExchangeData.Statics.KoreanBankRes::TIME,
                        data -> Double.parseDouble(data.DATA_VALUE())
                ));

        Double todayRate = exchangeRates.get(now);
        Double forCompareRate = exchangeRates.get(yesterday);
        Double changeRate = null;
        if(forCompareRate == null) {
            int i = 2;
            while(forCompareRate == null) {
                String date = LocalDateTime.now().minusDays(i++).format(DateTimeFormatter.BASIC_ISO_DATE);
                forCompareRate = exchangeRates.get(date);
            }
        }

        changeRate = ((todayRate - forCompareRate) / forCompareRate) * 100.0;

        List<ExchangeRateRes.Exchange> exchangeList = response.StatisticSearch().row().stream()
                .map(data -> new ExchangeRateRes.Exchange(
                        LocalDate.parse(data.TIME(), DateTimeFormatter.BASIC_ISO_DATE),
                        Double.parseDouble(data.DATA_VALUE())
                ))
                .toList();

        return new ExchangeRateRes(
                Country.getCurrencyType(currencyCode),
                todayRate,
                changeRate,
                exchangeList
        );
    }

    private void init(String now, String currencyCode) {
        this.url = "https://ecos.bok.or.kr/api/StatisticSearch/"
                + this.apiKey
                + "/json/kr/1/365/731Y001/D/" + LocalDateTime.now().minus(Period.of(1, 0, 0)).format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "/" + now + "/" + currencyCode;
        log.info("KoreanBank url : {}", this.url);
        this.client = WebClient.builder()
                .baseUrl(this.url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
