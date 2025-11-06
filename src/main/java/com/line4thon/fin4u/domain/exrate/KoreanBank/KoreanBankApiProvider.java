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

    public ExchangeRateRes.ExchangeRateData callExchangeRate(String currencyCode) {
        String now = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE);
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

        // 오늘 환율과 직전 영업일 환율을 가져와서 비교
        Double todayRate = exchangeRates.get(now);
        int i = 0;
        while(todayRate == null) {
            todayRate = exchangeRates.get(LocalDateTime.now().minusDays(i++).format(DateTimeFormatter.BASIC_ISO_DATE));
        }
        Double forCompareRate = exchangeRates.get(yesterday);
        Double changeRate = null;
        if(forCompareRate == null) {
            while(forCompareRate == null) {
                String date = LocalDateTime.now().minusDays(i++).format(DateTimeFormatter.BASIC_ISO_DATE);
                forCompareRate = exchangeRates.get(date);
            }
        }

        changeRate = ((todayRate - forCompareRate) / forCompareRate) * 100.0;

        // 1년간의 날짜와 환율을 필드로하는 객체 레퍼런스 배열 초기화
        List<ExchangeRateRes.Exchange> exchangeList = response.StatisticSearch().row().stream()
                .map(data -> new ExchangeRateRes.Exchange(
                        LocalDate.parse(data.TIME(), DateTimeFormatter.BASIC_ISO_DATE),
                        Double.parseDouble(data.DATA_VALUE())
                ))
                .toList();

        // 통화 코드별 각 은행사 최대 우대 환전 수수료를 객체 레퍼런스 배열로 초기화
        List<ExchangeRateRes.Fee> eachBankFee = initFees(currencyCode);

        return new ExchangeRateRes.ExchangeRateData(
                Country.getCurrencyType(currencyCode),
                todayRate,
                changeRate,
                exchangeList,
                eachBankFee
        );
    }

    private List<ExchangeRateRes.Fee> initFees(String currencyCode) {
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
