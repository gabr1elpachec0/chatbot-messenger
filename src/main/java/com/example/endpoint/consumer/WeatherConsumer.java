package com.example.endpoint.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class WeatherConsumer {
    private String APIKey = System.getenv("API_KEY");
    private String url = "https://api.openweathermap.org/data/2.5/weather?lat=-29.944&lon=-50.991&appid=" + APIKey + "&units=metric";

    private RestTemplate restTemplate = new RestTemplate();

    public String getWeatherAPIResponse() {
        ResponseEntity<String> weatherAPIResponse = restTemplate.getForEntity(url, String.class);
        int formattedTemp = 0;

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(weatherAPIResponse.getBody());

            double temp = Math.round(jsonNode.get("main").get("temp").asDouble());
            formattedTemp = (int) temp;

        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }

        return "A temperatura atual em Gravataí é de " + formattedTemp + "ºC";
    }
}
