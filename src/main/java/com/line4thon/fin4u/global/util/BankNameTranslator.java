package com.line4thon.fin4u.global.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BankNameTranslator {

    private static final Map<String, Map<String, String>> TRANSLATIONS = new HashMap<>();

    static {
        // KB Kookmin Bank
        Map<String, String> kb = Map.of(
                "en", "KB Bank",
                "zh", "国民银行",
                "vi", "Ngân hàng Kookmin"
        );
        TRANSLATIONS.put("KB_Bank", kb);

        Map<String, String> shinhan = Map.of(
                "en", "Shinhan Bank",
                "zh", "新韩银行",
                "vi", "Ngân hàng Shinhan"
        );
        TRANSLATIONS.put("Shinhan_Bank", shinhan);

        Map<String, String> woori = Map.of(
                "en", "Woori Bank",
                "zh", "友利银行",
                "vi", "Ngân hàng Woori"
        );
        TRANSLATIONS.put("Woori_Bank", woori);

        Map<String, String> hana = Map.of(
                "en", "Hana Bank",
                "zh", "韩亚银行",
                "vi", "Ngân hàng Hana"
        );
        TRANSLATIONS.put("Hana_Bank", hana);
    }

    public String translate(String englishBankName, String langCode) {
        Map<String, String> langMap = TRANSLATIONS.get(englishBankName);

        if (langMap == null) {
            return englishBankName;
        }

        String translatedName = langMap.get(langCode.toLowerCase());
        //기본값 영어
        return translatedName != null ? translatedName : englishBankName;
    }

}
