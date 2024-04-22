package com.example.messengerendpoint.response.variations;

import com.example.messengerendpoint.response.ResponseProvider;

public class WhatIsYourNameResponse implements ResponseProvider {
    @Override
    public String getResponse() {
        return "Meu nome é Gabriel Gomes Pacheco";
    }
}
