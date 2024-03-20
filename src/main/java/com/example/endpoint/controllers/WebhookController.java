package com.example.endpoint.controllers;

import com.example.endpoint.WebhookResponse;
import com.example.endpoint.webhook.WebhookEntry;
import com.example.endpoint.webhook.WebhookEntryMessaging;
import com.example.endpoint.webhook.WebhookObject;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
class WebhookController extends WebhookResponse {
    private String verifyToken = System.getenv("VERIFY_ACCESS_TOKEN");
    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/webhook")
    public ResponseEntity<WebhookObject> handleWebhookEvent(@RequestBody WebhookObject object) {
        String recipientId = "";
        String senderId = "";

        String message = "";

        if (object.getObject().equals("page")) {
            List<WebhookEntry> entryNode = object.getEntry();

            try {
                for (WebhookEntry entry : entryNode) {
                    recipientId = entry.getId();

                    List<WebhookEntryMessaging> messagingNode = entry.getMessaging();
                    for (WebhookEntryMessaging messaging : messagingNode) {
                        senderId = messaging.getSender().getId();

                        message = messaging.getMessage().getText();

                        String response = generateResponseForWebhook(message);

                        sendResponseForWebhook(senderId, response, recipientId);
                    }
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e);
            }
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
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
