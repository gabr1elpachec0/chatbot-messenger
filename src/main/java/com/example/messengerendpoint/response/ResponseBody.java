package com.example.messengerendpoint.response;

public class ResponseBody {
    private String recipientId;
    private String responseText;

    public ResponseBody(String recipientId, String responseText) {
        this.recipientId = recipientId;
        this.responseText = responseText;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public String buildBodyMessageForResponseType() {
        return """
        {
            "recipient": {
                "id": "%s"
            },
            "messaging_type": "RESPONSE",
            "message": {
                "text": "%s"
            }
        }
        """.formatted(recipientId, responseText);
    }
}