package com.example.messengerendpoint.response;

import com.example.messengerendpoint.VerificationStrategy;
import com.example.messengerendpoint.response.variations.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

public class WebhookResponse {
    private final String pageAccessToken = System.getenv("PAGE_ACCESS_TOKEN");
    private final RestTemplate restTemplate = new RestTemplate();
    private final List<VerificationStrategy> strategies;

    public WebhookResponse(List<VerificationStrategy> strategies) {
        this.strategies = strategies;
    }

    public String generateResponseForWebhook(String text) throws IOException {
        for (VerificationStrategy strategy : strategies) {
            if (strategy.isStrategy(text)) {
                return strategy.getResponse();
            }
        }
        return new ErrorResponse().getResponse();
    }

    public void sendResponseForWebhook(String body, String pageId) {
        try {
            String url = "https://graph.facebook.com/v19.0/" + pageId + "/messages?access_token=" + pageAccessToken;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        } finally {
            System.out.println("Mensagem enviada com sucesso!");
        }
    }
}
