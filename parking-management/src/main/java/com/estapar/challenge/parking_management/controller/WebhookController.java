package com.estapar.challenge.parking_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estapar.challenge.parking_management.dto.WebhookEventDTO;
import com.estapar.challenge.parking_management.service.EventProcessorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/webhook")
public class WebhookController {
    private final EventProcessorService eventProcessor;

    @PostMapping
    public ResponseEntity<Void> receiveWebhook(@RequestBody WebhookEventDTO event) {
        try {
            eventProcessor.process(event);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error processing webhook", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
