package com.example.endpoint.response.variations;

import com.example.endpoint.consumers.WeatherAPIConsumer;
import com.example.endpoint.response.ResponseProvider;

public class WeatherResponse extends WeatherAPIConsumer implements ResponseProvider {
    @Override
    public String getResponse() {
        try {
            String weatherResponse = getWeatherAPIResponse();
            return weatherResponse;
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return "Desculpe, não foi possível gerar a resposta";
    }
}
