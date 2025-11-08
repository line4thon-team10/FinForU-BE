package com.line4thon.fin4u.global.crawl.component;

import lombok.Getter;

import java.util.List;

@Getter
public class ParsedData {
    private String content;
    private List<String> newUrls;
}
