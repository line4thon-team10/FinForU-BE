package com.line4thon.fin4u.domain.guide.controller;

import com.line4thon.fin4u.domain.guide.dto.GetGuideMainPage;
import com.line4thon.fin4u.global.response.SuccessResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/guide")
public class AiGuideController {

    private final ChatClient chatClient;
    private final MessageSource messageSource;

    public AiGuideController(
            @Qualifier("mcpClient") ChatClient chatClient,
            @Qualifier("messageSource") MessageSource messageSource
    ) {
        this.chatClient = chatClient;
        this.messageSource = messageSource;
    }


    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getMain(
            Locale locale
    ) {
        GetGuideMainPage response;
        if(true) {
            List<GetGuideMainPage.Faq> questions = new ArrayList<>();
            questions.add(new GetGuideMainPage.Faq("How to open a bank account?"));
            questions.add(new GetGuideMainPage.Faq("What are bank rules by country?"));
            questions.add(new GetGuideMainPage.Faq("How do I stay safe from voice phishing or financial scams?"));

            response = new GetGuideMainPage(
                    "Hello. I'm your AI assistant.\n How can I help you today?",
                    questions
            );
        }
//        else if(user.getLanguage().equals("chinese")) {
//
//        } else if(user.getLanguage().equals("vietnamese")) {
//
//        }

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));
    }

    @GetMapping("/query")
    public ResponseEntity<SuccessResponse<?>> getAiGuide(
            // FIXME JWT 추가 시, 토큰 검증 필요
            @RequestParam @NotBlank String message
    ) {

        // FIXME 사용자의 언어 정보를 추가적으로 전달해주어 언어에 맞게 자연어처리 된 문장을 반환하도록 설정
        String response = chatClient.prompt(message).call().content();

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));
    }
}
