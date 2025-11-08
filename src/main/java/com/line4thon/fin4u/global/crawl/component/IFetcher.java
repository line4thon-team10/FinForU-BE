package com.line4thon.fin4u.global.crawl.component;

public interface IFetcher {
    String fetch(String url);
    Boolean supports(String url);
}
