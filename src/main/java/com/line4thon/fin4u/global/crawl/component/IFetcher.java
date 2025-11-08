package com.line4thon.fin4u.global.crawl.component;

public interface IFetcher {
    String fetch(String url, String bank);
    Boolean supports(String url);
}
