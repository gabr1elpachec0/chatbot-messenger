package com.example.messengerendpoint.response.variations;

import com.example.messengerendpoint.response.ResponseProvider;

public class GreetingResponse implements ResponseProvider {
    @Override
    public String getResponse() {
        return "Olá, sou um bot do Messenger!";
    }
}
