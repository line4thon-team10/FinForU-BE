package com.line4thon.fin4u.global.crawl;

import com.line4thon.fin4u.global.crawl.component.IFetcher;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DynamicPlaywrightFetcher implements IFetcher {

    @Autowired
    private Browser browser;

    @Override
    public String fetch(String url, String bank) {
        try (BrowserContext context = browser.newContext(new Browser.NewContextOptions().setLocale("ko-KR"))) {
            Page page = context.newPage();
            page.navigate(url, new Page.NavigateOptions().setTimeout(30000));

            page.selectOption("select", bank);
            page.locator("a").click();

            page.waitForSelector("tbody callback wait", new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(10000));

            return page.content();
        } catch (PlaywrightException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Boolean supports(String url) {
        return url.contains("portal.kfb.or.kr/minwon/freebranch_search.php?Branch_Type=B");
    }
}
