package com.line4thon.fin4u.global.crawl.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.line4thon.fin4u.global.crawl.dto.BankBranch;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class BankTableParser implements IParser {

    private final ObjectMapper objMapper = new ObjectMapper();

    @Override
    public ParsedData parse(String htmlContent, String baseUrl) {
        ParsedData data = new ParsedData();

        try {
            Document document = Jsoup.parse(htmlContent);

            Element table = document.selectFirst("#Table_Result");

            if(table == null) {
                data.setContent("");
                data.setNewUrls(new ArrayList<>());
                return data;
            }

            Elements rows = table.select("tbody tr");
            List<BankBranch> branches = new ArrayList<>();

            for (Element row : rows) {
                Elements cells = row.select("td");

                if(cells.size() >= 7) {
                    BankBranch branch = new BankBranch(
                            cells.get(0).text(),
                            cells.get(1).text(),
                            cells.get(2).text(),
                            cells.get(3).text(),
                            cells.get(4).text(),
                            cells.get(5).text(),
                            cells.get(6).text()
                    );

                    branches.add(branch);
                }
            }

            data.setContent(objMapper.writeValueAsString(branches));

            Elements links = document.select("a[href]");
            List<String> newUrls = links.stream()
                    .map(link -> link.absUrl("href"))
                    .distinct()
                    .toList();

            data.setNewUrls(newUrls);
        } catch (JsonProcessingException e) {
            log.error("Json Parse Failed", e);
            data.setContent("");
            data.setNewUrls(new ArrayList<>());
        }

        return data;
    }
}
