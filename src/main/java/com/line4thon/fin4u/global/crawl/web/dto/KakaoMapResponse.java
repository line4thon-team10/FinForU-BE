package com.line4thon.fin4u.global.crawl.web.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoMapResponse {

    private List<Document> documents;

    @Getter
    public static class Document {
        @JsonProperty("x")
        private String longitude;

        @JsonProperty("y")
        private String latitude;

        @JsonProperty("road_address")
        private RoadAddress roadAddress;
    }

    @Getter
    public static class RoadAddress {
        @JsonProperty("zone_no")
        private String zipCode;

    }
}