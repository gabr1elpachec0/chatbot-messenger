package com.example.messengerendpoint.response.variations;

import com.example.messengerendpoint.VerificationStrategy;
import com.example.messengerendpoint.response.DetectIntentTexts;
import com.google.cloud.dialogflow.v2.QueryResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AgeResponse implements VerificationStrategy {
    private final String sessionId = UUID.randomUUID().toString();

    @Override
    public boolean isStrategy(String string) throws IOException {
        List<String> texts = new ArrayList<String>();
        texts.add(string);

        Map<String, QueryResult> results = DetectIntentTexts.detectIntentTexts("messenger-bot-418619", texts, sessionId, "pt-BR");

        for (Map.Entry<String, QueryResult> entry : results.entrySet()) {
            QueryResult queryResult = entry.getValue();

            if (queryResult.getIntent().getDisplayName().equals("Age")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getResponse() {
        return "Eu tenho 18 anos de idade";
    }
}
