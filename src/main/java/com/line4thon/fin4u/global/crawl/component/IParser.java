package com.line4thon.fin4u.global.crawl.component;

public interface IParser {
    ParsedData parse(String htmlContent, String baseUrl);
}
