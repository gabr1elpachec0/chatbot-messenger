package com.example.endpoint.consumers;

import com.example.endpoint.weather.WeatherResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class WeatherAPIConsumer {
    private String APIKey = System.getenv("API_KEY");
    private String url = "https://api.openweathermap.org/data/2.5/weather?lat=-29.944&lon=-50.991&appid=" + APIKey + "&units=metric";

    private RestTemplate restTemplate = new RestTemplate();

    public String getWeatherAPIResponse() {
        int temperature = 0;
        int feelsLike = 0;

        String cityName = "";

        try {
            ResponseEntity<WeatherResponse> weatherAPIResponse = restTemplate.getForEntity(url, WeatherResponse.class);

            temperature = (int) weatherAPIResponse.getBody().getMain().getTemp();
            feelsLike = (int) weatherAPIResponse.getBody().getMain().getFeels_like();

            cityName = weatherAPIResponse.getBody().getName();

        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }

        return "A temperatura atual em " + cityName + " é de " + temperature + "ºC, com sensação térmica de " + feelsLike + "ºC";
    }
}
