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

    @PostMapping("/webhook")
    public ResponseEntity<WebhookObject> handleWebhookEvent(@RequestBody WebhookObject object) {
        if (object.getObject().equals("page")) {
            try {
                String recipientId = object.getRecipientId();
                String senderId = object.getSenderId();
                String message = object.getTextMessage();

                String response = generateResponseForWebhook(message);
                sendResponseForWebhook(senderId, response, recipientId);

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
