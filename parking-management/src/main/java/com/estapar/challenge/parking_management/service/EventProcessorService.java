package com.estapar.challenge.parking_management.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.estapar.challenge.parking_management.dto.WebhookEventDTO;
import com.estapar.challenge.parking_management.usecase.entry.EntryUseCase;
import com.estapar.challenge.parking_management.usecase.exit.ExitUseCase;
import com.estapar.challenge.parking_management.usecase.parked.ParkedUseCase;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventProcessorService {

    private final EntryUseCase entryUseCase;
    private final ParkedUseCase parkedUseCase;
    private final ExitUseCase exitUseCase;

    public void process(WebhookEventDTO event) {

        String eventType = event.getEventType();
        String plate = event.getLicensePlate();

        log.info("Processing {} event for license plate {}", eventType, plate);

        String normalizedEventType = normalizeEventType(eventType);

        log.info("Original event: '{}'", eventType);
        log.info("Normalized event: '{}'", normalizedEventType);

        switch (normalizedEventType) {

            case "ENTRY" -> entryUseCase.execute(event);

            case "PARKED" -> parkedUseCase.execute(event);

            case "EXIT" -> exitUseCase.execute(event);

            default -> log.warn("Unknown event {}", eventType);
        }
    }

    public String normalizeEventType(String event) {
        if (event == null) {
            return "UNKNOWN";
        }

        String normalized = event.trim().toUpperCase().split(" ")[0];
        if (normalized.matches("ENTRY|PARKED|EXIT")) {
            return normalized;
        }

        return "UNKNOWN";
    }
}