package com.example.endpoint.consumers;

import com.example.endpoint.weather.WeatherResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class WeatherAPIConsumer {
    private final String APIKey = System.getenv("API_KEY");
    private final String url = "https://api.openweathermap.org/data/2.5/weather?lat=-29.944&lon=-50.991&appid=" + APIKey + "&units=metric";
    private int temperature = 0;
    private int thermalSensation = 0;
    private String cityName = "";
    private final RestTemplate restTemplate = new RestTemplate();

    public String getWeatherAPIResponse() {
        try {
            ResponseEntity<WeatherResponse> weatherAPIResponse = restTemplate.getForEntity(url, WeatherResponse.class);

            temperature = (int) weatherAPIResponse.getBody().getMain().getTemp();
            thermalSensation = (int) weatherAPIResponse.getBody().getMain().getFeelsLike();
            cityName = weatherAPIResponse.getBody().getCityName();

        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }

        return "A temperatura atual em " + cityName + " é de " + temperature + "ºC, com sensação térmica de " + thermalSensation + "ºC";
    }
}
