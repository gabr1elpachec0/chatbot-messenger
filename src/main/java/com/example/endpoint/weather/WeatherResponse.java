package com.example.endpoint.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    private WeatherResponseMain main;
    private String name;

    public WeatherResponseMain getMain() {
        return main;
    }

    public void setMain(WeatherResponseMain main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
