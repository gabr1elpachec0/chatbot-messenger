package com.example.endpoint.response.variations;

import com.example.endpoint.response.ResponseProvider;

public class ErrorResponse implements ResponseProvider {
    @Override
    public String getResponse() {
        return "Desculpe, ainda não consigo responder a isso";
    }
}
