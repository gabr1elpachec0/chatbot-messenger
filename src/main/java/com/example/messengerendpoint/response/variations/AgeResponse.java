package com.example.messengerendpoint.response.variations;

import com.example.messengerendpoint.response.ResponseProvider;

public class AgeResponse implements ResponseProvider {
    @Override
    public String getResponse() {
        return "Eu tenho 18 anos de idade";
    }
}
