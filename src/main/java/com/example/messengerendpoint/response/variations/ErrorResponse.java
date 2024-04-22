package com.example.messengerendpoint.response.variations;

import com.example.messengerendpoint.response.ResponseProvider;

public class ErrorResponse implements ResponseProvider {
    @Override
    public String getResponse() {
        return "Desculpe, ainda não consigo responder a isso";
    }
}
