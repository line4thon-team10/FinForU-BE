package com.line4thon.fin4u.domain.exrate.KoreanBank;

import com.line4thon.fin4u.domain.exrate.entity.ExchangeFee;
import com.line4thon.fin4u.domain.exrate.exception.KoreanBankApuResponseNullException;
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
import java.util.ArrayList;
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

        init(now, currencyCode);

        ExchangeData response = client.get()
                .uri(this.url)
                .retrieve()
                .bodyToMono(ExchangeData.class)
                .block();

        // WebClient는 비동기 통신이어서, .block() 메소드로 블로킹함수로 변환했지만
        // 저번 테스트 통신 중 API 통신 에러가 떴었음.
        if(response == null) { throw new KoreanBankApuResponseNullException(); }

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

        return new ExchangeRateRes(
                Country.getCurrencyType(currencyCode),
                todayRate,
                changeRate,
                exchangeList,
                eachBankFee
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
