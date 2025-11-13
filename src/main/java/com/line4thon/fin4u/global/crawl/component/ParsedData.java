package com.line4thon.fin4u.global.crawl.component;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ParsedData {
    private String content;
    private List<String> newUrls;
}
