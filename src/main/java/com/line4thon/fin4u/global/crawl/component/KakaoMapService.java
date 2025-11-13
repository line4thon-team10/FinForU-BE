package com.line4thon.fin4u.global.crawl.component;

import com.line4thon.fin4u.global.crawl.web.dto.KakaoMapResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoMapService {

    private final WebClient kakaoClient;

    @Value("${kakao-map.api-key}")
    private String kakaoApiKey;
    private String KAKAO_URL = "https://dapi.kakao.com/v2/local/search/address.json";

    public KakaoMapResponse.Document getCoordinate(String address) {
//        log.info("getCoordinate.address: {}", address);
        KakaoMapResponse response = kakaoClient.get()
                .uri(KAKAO_URL, uri -> uri
                        .queryParam("query", address)
                        .queryParam("page", 1)
                        .queryParam("size", 10)
                        .build())
                .header("Authorization", buildKey())
                .retrieve()
                .bodyToMono(KakaoMapResponse.class)
                .block();

        if(response != null && !response.getDocuments().isEmpty()) {
//            log.info("Kakao API zone_no: {}", response.getDocuments().getFirst().getRoadAddress().getZipCode());
//            log.info("Kakao API longitude: {}", response.getDocuments().getFirst().getLongitude());
//            log.info("Kakao API latitude: {}", response.getDocuments().getFirst().getLatitude());
            return response.getDocuments().getFirst();
        }
        return null;
    }

    private String buildKey() {
        return "KakaoAK " + kakaoApiKey;
    }
}
