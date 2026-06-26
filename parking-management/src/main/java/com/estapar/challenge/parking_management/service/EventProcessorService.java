package com.estapar.challenge.parking_management.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.estapar.challenge.parking_management.dto.WebhookEventDTO;
import com.estapar.challenge.parking_management.repository.GarageRepository;
import com.estapar.challenge.parking_management.repository.SpotRepository;
import com.estapar.challenge.parking_management.repository.TicketRepository;

import jakarta.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventProcessorService {

    private final SpotRepository spotRepository;
    private final TicketRepository ticketRepository;
    private final GarageRepository garageRepository;

    @Transactional
    public void process(WebhookEventDTO event) {
        String eventType = event.getEventType();
        String plate = event.getLicensePlate();

        log.info("Processing event: {} for board {}", eventType, plate);

        if ("ENTRY".equals(eventType)) {
            processEntry(event);
        } else {
            log.info("Event {} still not implemented, only logging", eventType);
        }
    }

    @Transactional
    private void processEntry(WebhookEventDTO event) {
        String plate = event.getLicensePlate();
        if (ticketRepository.findByLicensePlateAndExitTimeIsNull(plate).isPresent()) {
            log.warn("Vehicle {} is already parked! Ignoring entry.", plate);
            return;
        }

        String sector = null; // implement findAvailableSector
        if (sector == null) {
            log.warn("Parking lot FULL! Unable to park {}", plate);
            return;
        }
    }
}