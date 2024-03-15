package com.example.endpoint.controllers;

import com.example.endpoint.Response;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
class WebhookController extends Response {
    private String verifyToken = System.getenv("VERIFY_ACCESS_TOKEN");

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhookEvent(@RequestBody JsonNode payload) {
        JsonNode objectNode = payload.get("object");
        JsonNode entryNode = payload.get("entry");

        if (objectNode != null && objectNode.asText().equals("page")) {
            if (entryNode != null && entryNode.isArray()) {
                for (JsonNode entry : entryNode) {
                    JsonNode messagingNode = entry.get("messaging");
                    String pageId = entry.get("id").asText();

                    if (messagingNode != null && messagingNode.isArray()) {
                        for (JsonNode messaging : messagingNode) {
                            JsonNode messageNode = messaging.get("message");
                            JsonNode senderNode = messaging.get("sender");

                            if (messageNode != null && messageNode.has("text")) {
                                String text = messageNode.get("text").asText();
                                String senderId = senderNode.get("id").asText();

                                if (text != null) {
                                    String responseText = generateResponse(text);
                                    sendResponse(senderId, responseText, pageId);
                                }
                            }
                        }
                    }
                }
            }
            return new ResponseEntity<>("EVENT_RECEIVED", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/webhook")
    public ResponseEntity<String> handleValidationVerification(
            @RequestParam(name = "hub.mode") String mode,
            @RequestParam(name = "hub.verify_token") String token,
            @RequestParam(name = "hub.challenge") int challenge
    ) {
        if (mode != null && token != null) {
            if (mode.equals("subscribe") && token.equals(verifyToken)) {
                System.out.println("WEBHOOK_VERIFIED");
                System.out.println("CHALLENGE_ACCEPTED");
                return ResponseEntity.ok(Integer.toString(challenge));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
