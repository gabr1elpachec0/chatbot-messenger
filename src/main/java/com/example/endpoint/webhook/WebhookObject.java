package com.example.endpoint.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookObject {
    private String object;
    private List<WebhookEntry> entry;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<WebhookEntry> getEntry() {
        return entry;
    }

    public void setEntry(List<WebhookEntry> entry) {
        this.entry = entry;
    }
}
