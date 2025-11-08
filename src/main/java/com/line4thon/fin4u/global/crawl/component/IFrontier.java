package com.line4thon.fin4u.global.crawl.component;

public interface IFrontier {
    void add(String url);
    String next();
}
