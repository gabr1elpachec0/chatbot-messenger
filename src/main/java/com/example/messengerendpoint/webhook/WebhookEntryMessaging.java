package com.example.messengerendpoint.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookEntryMessaging {
    private WebhookEntryMessagingSender sender;
    private WebhookEntryMessagingMessage message;

    public WebhookEntryMessagingSender getSender() {
        return sender;
    }

    public void setSender(WebhookEntryMessagingSender sender) {
        this.sender = sender;
    }

    public WebhookEntryMessagingMessage getMessage() {
        return message;
    }

    public void setMessage(WebhookEntryMessagingMessage message) {
        this.message = message;
    }
}
