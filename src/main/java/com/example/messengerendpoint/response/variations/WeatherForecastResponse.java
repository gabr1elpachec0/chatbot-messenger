package com.example.messengerendpoint.response.variations;

import com.example.messengerendpoint.consumers.WeatherAPIConsumer;

public class WeatherForecastResponse extends WeatherAPIConsumer {
    public String getResponse(String cityName, String date) {
        try {
            return getWeatherForecast(cityName, date);
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return "Desculpe, não foi possível gerar a resposta";
    }
}