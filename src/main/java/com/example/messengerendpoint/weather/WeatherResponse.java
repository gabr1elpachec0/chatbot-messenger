package com.example.messengerendpoint.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    private WeatherResponseMain main;
    @JsonProperty("name")
    private String cityName;

    public WeatherResponseMain getMain() {
        return main;
    }

    public void setMain(WeatherResponseMain main) {
        this.main = main;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}