package com.estapar.challenge.parking_management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.estapar.challenge.parking_management.dto.WebhookEventDTO;

@Slf4j
@Service
public class EventProcessorService {

    public void process(WebhookEventDTO event) {
        try {
            log.info("Processing event: {}", event);
        } catch (Exception e) {
            log.error("Error processing event: {}", event, e);
        }
    }
}