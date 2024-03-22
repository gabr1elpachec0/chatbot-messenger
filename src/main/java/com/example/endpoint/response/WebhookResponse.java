package com.example.endpoint.response;

import com.example.endpoint.response.variations.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WebhookResponse {
    private String textMessage;
    private final String pageAccessToken = System.getenv("PAGE_ACCESS_TOKEN");
    private final RestTemplate restTemplate = new RestTemplate();

    public String generateResponseForWebhook(String text) {
        textMessage = text;

        if (hasPattern("\\b(?:oi|ol[aá])\\b")) {
            return new GreetingResponse().getResponse();
        } else if (hasPattern("\\b(?:nome|chama)")) {
            return new WhatIsYourNameResponse().getResponse();
        } else if (hasPattern("\\b(?:idade|anos)\\b")) {
            return new AgeResponse().getResponse();
        } else if (hasPattern("\\b(?:cidade|tempo|temperatura)\\b")) {
            return new WeatherResponse().getResponse();
        } else {
            return new ErrorResponse().getResponse();
        }
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

    private boolean hasPattern(String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(textMessage);
        return matcher.find();
    }
}
