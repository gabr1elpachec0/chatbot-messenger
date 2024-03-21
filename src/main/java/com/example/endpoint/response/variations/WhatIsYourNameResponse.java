package com.example.endpoint.response.variations;

import com.example.endpoint.response.ResponseProvider;

public class WhatIsYourNameResponse implements ResponseProvider {
    @Override
    public String getResponse() {
        return "Meu nome é Gabriel Gomes Pacheco";
    }
}
