package com.example.endpoint.response.variations;

import com.example.endpoint.response.ResponseProvider;

public class GreetingResponse implements ResponseProvider {
    @Override
    public String getResponse() {
        return "Olá, sou um bot do Messenger!";
    }
}
