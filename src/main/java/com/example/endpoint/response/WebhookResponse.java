package com.example.endpoint.response;

import com.example.endpoint.consumers.WeatherAPIConsumer;
import com.example.endpoint.response.variations.AgeResponse;
import com.example.endpoint.response.variations.GreetingResponse;
import com.example.endpoint.response.variations.WeatherResponse;
import com.example.endpoint.response.variations.WhatIsYourNameResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WebhookResponse {
    private String textMessage;
    private RestTemplate restTemplate = new RestTemplate();

    public String generateResponseForWebhook(String text) {
        textMessage = text;

        if (hasPattern("\\b(?:oi|ola)\\b")) {
            return new GreetingResponse().getResponse();
        } else if (hasPattern("\\b(?:nome|chama)")) {
            return new WhatIsYourNameResponse().getResponse();
        } else if (hasPattern("\\b(?:idade|anos)\\b")) {
            return new AgeResponse().getResponse();
        } else if (hasPattern("\\b(?:cidade|tempo|temperatura)\\b")) {
            return new WeatherResponse().getResponse();
        } else {
            return "Desculpe, ainda não consigo responder a isso";
        }
    }

    private boolean hasPattern(String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(textMessage);
        return matcher.find();
    }

    public void sendResponseForWebhook(String recipientId, String responseText, String pageId) {
        String pageAccessToken = System.getenv("PAGE_ACCESS_TOKEN");

        String url = "https://graph.facebook.com/v19.0/" + pageId + "/messages?access_token=" + pageAccessToken;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{\"recipient\":{\"id\":\"" + recipientId + "\"},\"messaging_type\":\"RESPONSE\",\"message\":{\"text\":\"" + responseText + "\"}}";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Mensagem enviada com sucesso!");
        } else {
            System.out.println("Erro ao enviar mensagem: " + response.getBody());
        }
    }
}
