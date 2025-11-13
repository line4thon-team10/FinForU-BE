package com.line4thon.fin4u.global.crawl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import crawlercommons.robots.BaseRobotRules;
import crawlercommons.robots.SimpleRobotRules;
import crawlercommons.robots.SimpleRobotRulesParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RobotsTxtService {

    private static final String USER_AGENT = "fin4uCrawler";

    private final LoadingCache<String, BaseRobotRules> rulesCache;
    private final HttpClient client;

    public RobotsTxtService(HttpClient httpClient) {
        this.client = httpClient;
        this.rulesCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(host -> fetchAndParseRobotsTxt(host));
    }

    public Boolean isAllowed(String url) {
        try {
            URL urlObj = new URL(url);
            String host = urlObj.getHost();
            BaseRobotRules rules = rulesCache.get(host);

            return rules.isAllowed(url);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    private BaseRobotRules fetchAndParseRobotsTxt(String host) {
        String robotsTxtUrl = "https://" + host + "/robots.txt";

        try {
            HttpGet request = new HttpGet(robotsTxtUrl);
            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            if(statusCode >= 200 && statusCode < 300) {
                byte[] content = EntityUtils.toByteArray(response.getEntity());
                String contentType = response.getFirstHeader("Content-Tyoe").getValue();

                SimpleRobotRulesParser parser = new SimpleRobotRulesParser();

                return parser.parseContent(robotsTxtUrl, content, contentType,
                        List.of(USER_AGENT.toLowerCase(), "*"));
            } else {
                return handleFailedFetch(statusCode);
            }
        } catch (ClientProtocolException e) {
            return new SimpleRobotRules(SimpleRobotRules.RobotRulesMode.ALLOW_NONE);
        } catch (IOException e) {
            return new SimpleRobotRules(SimpleRobotRules.RobotRulesMode.ALLOW_NONE);
        }
    }

    private BaseRobotRules handleFailedFetch(int statusCode) {
        if(statusCode >= 400 && statusCode < 500) {
            return new SimpleRobotRules(SimpleRobotRules.RobotRulesMode.ALLOW_ALL);
        } else {
            return new SimpleRobotRules(SimpleRobotRules.RobotRulesMode.ALLOW_NONE);
        }
    }

}
