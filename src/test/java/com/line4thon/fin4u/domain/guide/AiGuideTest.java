package com.line4thon.fin4u.domain.guide;

import com.line4thon.fin4u.config.TestSecurityConfig;
import com.line4thon.fin4u.domain.guide.controller.AiGuideController;
import com.line4thon.fin4u.global.config.MessageSourceConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AiGuideController.class)
@Import(MessageSourceConfig.class)
public class AiGuideTest {

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private MockMvc mock;


    @MockBean(name = "mcpClient", answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient client;

    @Test
    @DisplayName("AI 가이드 메인 페이지 로드")
    void getMainSuccess() throws Exception {
        mock.perform(get("/guide")
                        .with(user("test"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.helloMessage")
                        .isNotEmpty())
                .andExpect(jsonPath("$.data.questions").isArray())
                .andExpect(jsonPath("$.data.questions[0].question").isNotEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("AI 가이드 자연어 질의")
    void getAiGuideSuccess() throws Exception {
        String testMessage = "How can I open a bank account in Korea?";
        String aiResponse = "To open a bank account in Korea, you need your passport and alien registration card";

        given(client.prompt(anyString()).call().content()).willReturn(aiResponse);
        mock.perform(get("/guide/query")
                        .with(user("test"))
                        .param("message", testMessage)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.isSuccess").value(true))
                        .andExpect(jsonPath("$.data").value(aiResponse))
                        .andDo(print());

    }

    @Test
    @DisplayName("AI에게 질의할 때, \'message == null\'인 케이스")
    void getAiGuideFailWhenMessageIsBlank() throws Exception {
        mock.perform(get("/guide/query")
                        .with(user("test"))
                        .param("message", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}