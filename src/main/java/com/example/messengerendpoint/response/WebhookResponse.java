package com.example.messengerendpoint.response;

import com.example.messengerendpoint.response.variations.*;
import com.google.cloud.dialogflow.v2.QueryResult;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public abstract class WebhookResponse {
    private String textMessage;
    private final String pageAccessToken = System.getenv("PAGE_ACCESS_TOKEN");
    private final RestTemplate restTemplate = new RestTemplate();
    private String sessionId = UUID.randomUUID().toString();
    private String cityName = "";
    private String date = "";

    public String generateResponseForWebhook(String text) throws IOException {
        textMessage = text;
        List<String> texts = new ArrayList<String>();
        texts.add(text);

        Map<String, QueryResult> results = com.example.endpoint.response.DetectIntentTexts.detectIntentTexts("messenger-bot-418619", texts, sessionId, "pt-BR");

        for (Map.Entry<String, QueryResult> entry : results.entrySet()) {
            QueryResult queryResult = entry.getValue();

            if (queryResult.getIntent().getDisplayName().equals("Greetings")) {
                return new GreetingResponse().getResponse();
            } else if (queryResult.getIntent().getDisplayName().equals("Name")) {
                return new WhatIsYourNameResponse().getResponse();
            } else if (queryResult.getIntent().getDisplayName().equals("Age")) {
                return new AgeResponse().getResponse();
            } else if (queryResult.getIntent().getDisplayName().equals("Weather")) {
                String fullfillmentText = queryResult.getFulfillmentText();
                if (fullfillmentText != "") {
                    return fullfillmentText;
                } else {
                    cityName = queryResult.getParameters().getFieldsOrThrow("city").getStringValue();
                    date = queryResult.getParameters().getFieldsOrThrow("date-time").getStringValue();

                    LocalDate correctDate = LocalDate.parse(date.split("T")[0]);

                    LocalDate now = LocalDate.now();

                    if (correctDate.equals(now)) {
                        return new CurrentWeatherResponse().getCurrentWeatherResponse(cityName);
                    } else {
                        return new WeatherForecastResponse().getResponse(cityName, date);
                    }
                }
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
