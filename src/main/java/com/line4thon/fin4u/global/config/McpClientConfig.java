package com.line4thon.fin4u.global.config;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class McpClientConfig {

    @Bean
    public ChatClient mcpClient(ChatClient.Builder chatClientBuilder, List<McpSyncClient> mcpSyncClients) {
        return chatClientBuilder
                .defaultSystem("당신은 외국인 유저들에게 금융 가이드를 알려주는 친절한 ChatBot Service의 AI입니다." +
                        "정보는 Qdrant DB에서 찾아보고, 없다면 없다고 전달해야 합니다." +
                        "DB에서 검색하되, 유저에게 전달할 때에는 \'제가 검색해본 바로는\'과 같은" +
                        "접두어는 붙이지 말아야 합니다.")
                .defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpSyncClients))
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder().build()).build())
                .build();
    }
}
