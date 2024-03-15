package com.example.endpoint;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.coyote.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EndpointApplication {

	public static void main(String[] args) {
		SpringApplication.run(EndpointApplication.class, args);
	}
}


@RestController
class WebhookController {
	private String verifyToken = System.getenv("VERIFY_ACCESS_TOKEN");

	private String generateResponse(String text) {
		if (text.equalsIgnoreCase("Qual o seu nome?")) {
			return "Meu nome é Gabriel Gomes Pacheco";
		} else if (text.equalsIgnoreCase("Qual a sua idade?")) {
			return "Eu tenho 18 anos de idade";
		} else {
			return "Desculpe, não sei responder a essa pergunta";
		}
	}

	private RestTemplate restTemplate = new RestTemplate();

	private void sendResponse(String recipientId, String responseText) {
		String pageAccessToken = System.getenv("PAGE_ACCESS_TOKEN");
		//System.out.println(pageAccessToken);
		String url = "https://graph.facebook.com/v19.0/112796213566995/messages?access_token=" + pageAccessToken;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String body = "{\"recipient\":{\"id\":\"" + recipientId + "\"},\"messaging_type\":\"RESPONSE\",\"message\":{\"text\":\"" + responseText + "\"}}";

		HttpEntity<String> request = new HttpEntity<>(body, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			System.out.println("Mensagem enviada com sucesso!");
		} else {
			System.out.println("Erro ao enviar mensagem: " + response.getBody());
		}
	}

	@PostMapping("/webhook")
	public ResponseEntity<String> handleWebhookEvent(@RequestBody JsonNode payload) {
		JsonNode objectNode = payload.get("object");
		JsonNode entryNode = payload.get("entry");
		//String recipientId = "7871157799602319";

		if (objectNode != null && objectNode.asText().equals("page")) {
			if (entryNode != null && entryNode.isArray()) {
				for (JsonNode entry : entryNode) {
					JsonNode messagingNode = entry.get("messaging");

					if (messagingNode != null && messagingNode.isArray()) {
						for (JsonNode messaging : messagingNode) {
							JsonNode messageNode = messaging.get("message");
							JsonNode senderNode = messaging.get("sender");

							if (messageNode != null && messageNode.has("text")) {
								String text = messageNode.get("text").asText();
								String senderId = senderNode.get("id").asText();

								if (text != null) {
									String responseText = generateResponse(text);
									sendResponse(senderId, responseText);
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
	)
	{
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