package com.example.messengerendpoint.response.variations;

import com.example.messengerendpoint.VerificationStrategy;
import com.example.messengerendpoint.response.DetectIntentTexts;
import com.google.cloud.dialogflow.v2.QueryResult;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class WeatherResponse implements VerificationStrategy {
    private final String sessionId = UUID.randomUUID().toString();
    private String cityName = "";
    private String date = "";
    private String fullfillmentText = "";

    @Override
    public boolean isStrategy(String string) throws IOException {
        List<String> texts = new ArrayList<String>();
        texts.add(string);

        Map<String, QueryResult> results = DetectIntentTexts.detectIntentTexts("messenger-bot-418619", texts, sessionId, "pt-BR");

        for (Map.Entry<String, QueryResult> entry : results.entrySet()) {
            QueryResult queryResult = entry.getValue();

            if (queryResult.getIntent().getDisplayName().equals("Weather")) {
                fullfillmentText = queryResult.getFulfillmentText();
                cityName = queryResult.getParameters().getFieldsOrThrow("city").getStringValue();
                date = queryResult.getParameters().getFieldsOrThrow("date-time").getStringValue();

                return true;
            }
        }
        return false;
    }

    @Override
    public String getResponse() {
        if (!Objects.equals(fullfillmentText, "")) {
            return fullfillmentText;
        } else {
            LocalDate correctDate = LocalDate.parse(date.split("T")[0]);

            LocalDate now = LocalDate.now();

            if (correctDate.equals(now)) {
                return new CurrentWeatherResponse().getCurrentWeatherResponse(cityName);
            } else {
                return new WeatherForecastResponse().getResponse(cityName, date);
            }
        }
    }
}
