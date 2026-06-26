package com.estapar.challenge.parking_management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.estapar.challenge.parking_management.dto.WebhookEventDTO;

import jakarta.transaction.Transactional;

@Slf4j
@Service
public class EventProcessorService {

    @Transactional
    public void process(WebhookEventDTO event) {
        String eventType = event.getEventType();
        String plate = event.getLicensePlate();

        log.info("Processing event: {} for board {}", eventType, plate);

        if ("ENTRY".equals(eventType)) {
            // processEntry(event);
        } else {
            log.info("Event {} still not implemented, only logging", eventType);
        }
    }
}