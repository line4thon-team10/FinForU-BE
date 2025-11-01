package com.line4thon.fin4u.domain.guide.controller;

import com.line4thon.fin4u.global.response.SuccessResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guide")
@RequiredArgsConstructor
public class AiGuideController {

    private final ChatClient chatClient;

    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getAiGuide(
            // FIXME JWT 추가 시, 토큰 검증 필요
            @RequestParam @NotBlank String message
    ) {

        String response = chatClient.prompt(message).call().content();

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.empty());
    }
}
