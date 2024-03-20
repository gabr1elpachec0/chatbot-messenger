package com.example.endpoint.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookObject {
    private String object;
    private String recipientId;
    private String senderId;
    private String textMessage;
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

    public String getRecipientId() {
        for (WebhookEntry entry : entry) {
            recipientId = entry.getId();
        }
        return recipientId;
    }

    public String getSenderId() {
        for (WebhookEntry entry : entry) {
            List<WebhookEntryMessaging> messagingNode = entry.getMessaging();
            for (WebhookEntryMessaging messaging : messagingNode) {
                senderId = messaging.getSender().getId();
            }
        }
        return senderId;
    }

    public String getTextMessage() {
        for (WebhookEntry entry : entry) {
            List<WebhookEntryMessaging> messagingNode = entry.getMessaging();
            for (WebhookEntryMessaging messaging : messagingNode) {
                textMessage = messaging.getMessage().getText();
            }
        }
        return textMessage;
    }
}
