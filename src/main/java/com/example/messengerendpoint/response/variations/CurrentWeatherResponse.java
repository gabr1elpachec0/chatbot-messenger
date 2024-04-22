package com.example.messengerendpoint.response.variations;

import com.example.messengerendpoint.consumers.WeatherAPIConsumer;

public class CurrentWeatherResponse extends WeatherAPIConsumer {

    public String getResponse(String cityName) {
        try {
            return getCurrentWeatherResponse(cityName);
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return "Desculpe, não foi possível gerar a resposta";
    }
}