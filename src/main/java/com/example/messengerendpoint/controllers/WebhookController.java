package com.example.messengerendpoint.controllers;

import com.example.messengerendpoint.VerificationStrategy;
import com.example.messengerendpoint.response.ResponseBody;
import com.example.messengerendpoint.response.WebhookResponse;
import com.example.messengerendpoint.response.variations.AgeResponse;
import com.example.messengerendpoint.response.variations.GreetingResponse;
import com.example.messengerendpoint.response.variations.WeatherResponse;
import com.example.messengerendpoint.response.variations.WhatIsYourNameResponse;
import com.example.messengerendpoint.webhook.WebhookObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
class WebhookController extends WebhookResponse {
    private final String verifyToken = System.getenv("VERIFY_ACCESS_TOKEN");

    private final List<VerificationStrategy> strategies = List.of(
            new AgeResponse(),
            new GreetingResponse(),
            new WhatIsYourNameResponse(),
            new WeatherResponse()
    );

    private final WebhookResponse webhookResponse = new WebhookResponse(strategies);

    public WebhookController(List<VerificationStrategy> strategies) {
        super(strategies);
    }

    @GetMapping("/webhook")
    public ResponseEntity<String> handleVerification (
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

    @PostMapping("/webhook")
    public ResponseEntity<WebhookObject> handleWebhookEvent(@RequestBody WebhookObject object) {
        if (object.getObject().equals("page")) {
            try {
                String pageId = object.getPageId();
                String senderId = object.getSenderId();
                String textMessage = object.getTextMessage();

                String response = webhookResponse.generateResponseForWebhook(textMessage);

                ResponseBody responseBody = new ResponseBody(senderId, response);
                String body = responseBody.buildBodyMessageForResponseType();

                sendResponseForWebhook(body, pageId);
            } catch (Exception e) {
                System.out.println("Erro: " + e);
            }
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
