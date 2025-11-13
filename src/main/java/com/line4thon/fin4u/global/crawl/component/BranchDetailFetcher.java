package com.line4thon.fin4u.global.crawl.component;

import com.line4thon.fin4u.global.crawl.web.dto.BranchDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BranchDetailFetcher {
    private final HttpClient httpClient;
    private static final String DETAIL_URL = "https://portal.kfb.or.kr/minwon/freebranch_detail.php";

    public BranchDetail getPhoneNum(String branchId) {
        if (branchId == null || branchId.isEmpty()) {
            return null;
        }

        HttpPost request = new HttpPost(DETAIL_URL);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        request.setHeader("Referer", "https://portal.kfb.or.kr/minwon/freebranch_search.php?Branch_Type=B");

        try {
            List<NameValuePair> payload = new ArrayList<>();
            payload.add(new BasicNameValuePair("idx", branchId)); // 사용자가 찾아낸 "idx"
            request.setEntity(new UrlEncodedFormEntity(payload, "UTF-8"));

            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            String popupHtml = EntityUtils.toString(entity);

            Document popupDoc = Jsoup.parse(popupHtml);
            Element headerTh = popupDoc.selectFirst("th:contains(연락처)");

            String phoneNum = "";

            if (headerTh!= null) {
                Element phoneTd = headerTh.parent().selectFirst("td.tl");
                if (phoneTd!= null) {
                    phoneNum = phoneTd.ownText().trim(); // "02)2279_9101"
                }
            }

            String operatingTime = "";
            Element timeHeaderTh = popupDoc.selectFirst("th:contains(영업시간)");
            if(timeHeaderTh != null) {
                Element timeId = timeHeaderTh.parent().selectFirst("td.tl");
                if (timeId != null)
                    operatingTime = timeId.text().trim();
            }

            EntityUtils.consume(entity); // 응답 소모

            return new BranchDetail(phoneNum, operatingTime);
        } catch (ClientProtocolException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("은행연합회 사이트 접속 불가");
            throw new RuntimeException(e);
        }
    }
}
