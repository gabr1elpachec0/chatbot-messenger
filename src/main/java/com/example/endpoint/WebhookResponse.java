package com.example.endpoint;

import com.example.endpoint.consumers.WeatherAPIConsumer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.Normalizer;

public abstract class WebhookResponse extends WeatherAPIConsumer {
    private RestTemplate restTemplate = new RestTemplate();

    public String generateResponseForWebhook(String text) {
        text = Normalizer.normalize(text.toLowerCase(), Normalizer.Form.NFD);

        if (text.contains("nome") || text.contains("chama")) {
            return "Meu nome é Gabriel Gomes Pacheco";
        } else if (text.contains("tempo") || text.contains("temperatura") || text.contains("cidade")) {
            try {
                String weatherResponse = getWeatherAPIResponse();
                return weatherResponse;
            } catch (Exception e) {
                System.out.println("Erro: " + e);
            }
            return "Desculpe, não foi possível gerar a resposta";
        } else if (text.contains("idade") || text.contains("anos")) {
            return "Eu tenho 18 anos de idade";
        } else if (text.contains("oi") || text.contains("ola")) {
            return "Olá, sou um bot do Messenger!";
        } else {
            return "Desculpe, ainda não consigo responder a isso";
        }
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
