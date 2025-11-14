package com.line4thon.fin4u.domain.recommend.fastApi;

import com.line4thon.fin4u.domain.recommend.fastApi.dto.AiRecommendRes;
import com.line4thon.fin4u.domain.recommend.fastApi.dto.AiRecommendReq;
import com.line4thon.fin4u.domain.recommend.fastApi.dto.UpsertRes;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Component
@Service
public class AiRecClient {

    @Qualifier("recommendClient")
    private final WebClient client;
    AiRecClient(WebClient recommendClient) { this.client =recommendClient; }

    //파이썬에 보낼 전체 상품 리스트 전송
    //벡터 db에 적재할 전체 상품 전송 api
    public String postUpsert(@RequestBody List<UpsertRes.UpsertItem> items) {
        UpsertRes.UpsertBody body = new UpsertRes.UpsertBody(items);

//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            String json = mapper.writeValueAsString(body);
//            log.info("===== SENDING TO PYTHON /upsert =====");
//            log.info(json);
//            log.info("======================================");
//        } catch (Exception e) {
//            log.error("Failed to serialize body", e);
//        }

        return client.post()
                .uri("/upsert")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    //사용자의 선호도를 전송하여 ai추천을 받음
    public AiRecommendRes recommend(AiRecommendReq req) {
        return client.post()
                .uri("/recommend")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(AiRecommendRes.class)
                .block();
    }
}