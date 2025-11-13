package com.line4thon.fin4u.global.config;

import com.line4thon.fin4u.global.crawl.component.IFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FetcherFactory {

    private final List<IFetcher> fetchers;

    public IFetcher getFetcherForUrl(String url) {
        return fetchers.stream()
                .filter(f -> f.supports(url))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No Fetcher found for URL: " + url ));
    }
}
