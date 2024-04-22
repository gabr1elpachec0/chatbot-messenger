package com.example.messengerendpoint.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookEntry {
    private String id;
    private List<WebhookEntryMessaging> messaging;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<WebhookEntryMessaging> getMessaging() {
        return messaging;
    }

    public void setMessaging(List<WebhookEntryMessaging> messaging) {
        this.messaging = messaging;
    }
}
