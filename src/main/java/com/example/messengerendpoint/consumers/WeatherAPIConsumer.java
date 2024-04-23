package com.example.messengerendpoint.consumers;

import com.example.messengerendpoint.weather.WeatherResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class WeatherAPIConsumer {
    private final String APIKey = System.getenv("API_KEY");
    private int temperature = 0;
    private int thermalSensation = 0;
    private final RestTemplate restTemplate = new RestTemplate();

    public String getCurrentWeatherResponse(String cityName) {
        String currentWeatherURL = "https://api.openweathermap.org/data/2.5/weather?units=metric&q=" + cityName + "&appid=" + APIKey;
        try {
            ResponseEntity<WeatherResponse> weatherAPIResponse = restTemplate.getForEntity(currentWeatherURL, WeatherResponse.class);

            temperature = (int) weatherAPIResponse.getBody().getMain().getTemp();
            thermalSensation = (int) weatherAPIResponse.getBody().getMain().getFeelsLike();
            cityName = weatherAPIResponse.getBody().getCityName();

        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }

        return "A temperatura atual em " + cityName + " é de " + temperature + "ºC, com sensação térmica de " + thermalSensation + "ºC";
    }

    public String getWeatherForecast(String cityName, String date) {
        String weatherForecastURL = "https://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&appid=" + APIKey + "&units=metric";

        try {
            ResponseEntity<JsonNode> weatherAPIResponse = restTemplate.getForEntity(weatherForecastURL, JsonNode.class);

            JsonNode forecastsNode = weatherAPIResponse.getBody().get("list");

            String datePart = date.substring(0, 10);
            int minTemperature = Integer.MAX_VALUE;
            int maxTemperature = Integer.MIN_VALUE;

            if (forecastsNode.isArray()) {
                for (JsonNode forecastNode : forecastsNode) {
                    String dateTime = forecastNode.get("dt_txt").asText();
                    String forecastDatePart = dateTime.substring(0, 10);

                    if (datePart.equals(forecastDatePart)) {
                        double temperature = forecastNode.get("main").get("temp").asDouble();
                        int roundedTemperature = (int) Math.round(temperature);
                        minTemperature = Math.min(minTemperature, roundedTemperature);
                        maxTemperature = Math.max(maxTemperature, roundedTemperature);
                    }
                }

                if (minTemperature != Integer.MAX_VALUE && maxTemperature != Integer.MIN_VALUE) {
                    return "A temperatura em " + cityName + " na data especificada terá mínima de " + minTemperature + " ºC e máxima de " + maxTemperature + " ºC.";
                }
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }

        return "Não há previsão de temperatura em " + cityName + " para a data especificada.";
    }
}
