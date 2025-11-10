package com.line4thon.fin4u.global.crawl.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.line4thon.fin4u.global.crawl.DynamicPlaywrightFetcher;
import com.line4thon.fin4u.global.crawl.Entity.ForeignerStore;
import com.line4thon.fin4u.global.crawl.web.dto.BankBranch;
import com.line4thon.fin4u.global.crawl.web.dto.BranchDetail;
import com.line4thon.fin4u.global.crawl.web.dto.KakaoMapResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlerItemProcessor implements ItemProcessor<String, List<ForeignerStore>> {

    private final String url = "https://portal.kfb.or.kr/minwon/freebranch_search.php?Branch_Type=B";

    private final DynamicPlaywrightFetcher fetcher;
    private final BankTableParser parser;
    private final BranchDetailFetcher branchDetailFetcher;
    private final ObjectMapper mapper;
    private final KakaoMapService kakaoService;

    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{2}:\\d{2}\\s*~\\s*\\d{2}:\\d{2})");

    @Override
    public List<ForeignerStore> process(String bankCode) throws InterruptedException {
        String mainHtml = fetcher.fetch(
                "https://portal.kfb.or.kr/minwon/freebranch_search.php?Branch_Type=B",
                bankCode
        );
        if(mainHtml.isBlank()) return null;

        ParsedData parsed = parser.parse(mainHtml, url);

        List<BankBranch> branches;
        try {
            branches = mapper.readValue(parsed.getContent(),
                    new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            log.error("Json 파싱 실패");

            return null;
        }

        List<ForeignerStore> response = new ArrayList<>();

        for(BankBranch branch : branches) {
            BranchDetail details = branchDetailFetcher.getPhoneNum(branch.getBranchId());
            String phoneNum = details.phoneNum();
            String operatingTime = details.operatingTime();


            Thread.sleep(1000);

            String originAddress = branch.getAddress();
//            log.info("origin address: {}", originAddress);
            String replaced = "";

            if(!originAddress.isBlank()) {
                int firstSpaceIndex = originAddress.indexOf(" ");
                if(firstSpaceIndex > -1)
                    replaced = originAddress.substring(firstSpaceIndex + 1).trim(); // 공백 제거
                else
                    replaced = originAddress; // 공백이 없는 주소(e.g., "서울")
            }

//            log.info("replaced: {}", replaced);

            KakaoMapResponse.Document kakaoDoc = kakaoService.getCoordinate(replaced);
            String zipCode = "";
            Double longitude = null;
            Double latitude = null;

            if (kakaoDoc != null) {
                if(kakaoDoc.getRoadAddress() != null)
                    zipCode = kakaoDoc.getRoadAddress().getZipCode();
                longitude = Double.parseDouble(kakaoDoc.getLongitude());
                latitude = Double.parseDouble(kakaoDoc.getLatitude());
            }

            Matcher matcher = TIME_PATTERN.matcher(operatingTime);
            List<String> times = new ArrayList<>();
            while (matcher.find()) {
                times.add(matcher.group(1));
            }

            if(times.size() == 1) {
                times.add(times.getFirst());
            }

//            log.info("BankName: {}", branch.getBankName());
//            log.info("BranchName: {}", branch.getBranchName());
//            log.info("zipCode: {}", zipCode);
//            log.info("weekClose: {}", times.getFirst());
//            log.info("weekendClose: {}", times.get(1));
//            log.info("phoneNum: {}", phoneNum);
//            log.info("longitude: {}", longitude);
//            log.info("latitude: {}", latitude);

            response.add(ForeignerStore.builder()
                    .name(branch.getBankName())
                    .zipCode(zipCode)
                    .weekClose(parseToTime(times.getFirst()))
                    .weekendClose(parseToTime(times.get(1)))
                    .phoneNum(phoneNum)
                    .longitude(longitude)
                    .latitude(latitude)
                    .build());
        }
        return response;
    }

    private LocalTime parseToTime(String operatingTime) {
//        log.info("operatingTime: {}", operatingTime);
        String[] split = operatingTime.split("~");
        String result = split[1].replaceAll("[^0-9:]", "");
        return LocalTime.parse(result, DateTimeFormatter.ofPattern("HH:mm"));
    }
}
