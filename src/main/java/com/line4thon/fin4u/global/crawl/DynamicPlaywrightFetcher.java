package com.line4thon.fin4u.global.crawl;

import com.line4thon.fin4u.global.crawl.component.IFetcher;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
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

//            String selectSelector = "select";
//            log.debug("[Fetcher] '{}' 드롭다운에서 '{}' 레이블 선택 시도...",selectSelector, bank);
//            page.selectOption(selectSelector, new SelectOption().setLabel(bank));
//            log.debug("[Fetcher] 은행 선택 완료: {}", bank);
//
//            log.debug("   [Fetcher] '검색' 버튼 클릭 시도...");
//            page.locator("a.btnSearch01").click();
//            log.debug("   [Fetcher] '검색' 버튼 클릭 완료.");
//
            page.selectOption("select#BankCode", bank);
            page.waitForNavigation(() -> {
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("검색").setExact(true)).click();
            });
            String tableSelector = "table.resultList_ty02 tbody tr";
//            log.debug("[Fetcher] 결과 테이블 '{}' 로드 대기...", tableSelector);
            page.waitForSelector(tableSelector,
                    new Page.WaitForSelectorOptions()
                            .setState(WaitForSelectorState.VISIBLE)
                            .setTimeout(10000));
            log.debug("[Fetcher] 결과 테이블 로드 완료.");

            /*
            page.locator("a").click();

            page.waitForSelector("#Table_Result tbody tr", new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(10000));
*/
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
