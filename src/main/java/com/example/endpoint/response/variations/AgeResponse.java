package com.example.endpoint.response.variations;

import com.example.endpoint.response.ResponseProvider;

public class AgeResponse implements ResponseProvider {
    @Override
    public String getResponse() {
        return "Eu tenho 18 anos de idade";
    }
}
