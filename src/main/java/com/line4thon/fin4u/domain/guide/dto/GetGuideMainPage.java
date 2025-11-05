package com.line4thon.fin4u.domain.guide.dto;

import java.util.List;

public record GetGuideMainPage(
        String helloMessage,
        List<Faq> questions
) {
    public record Faq(
            String question
    ) { }
}
