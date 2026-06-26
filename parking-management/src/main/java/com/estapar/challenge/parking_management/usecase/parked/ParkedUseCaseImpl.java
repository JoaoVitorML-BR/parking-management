package com.estapar.challenge.parking_management.usecase.parked;

import org.springframework.stereotype.Service;

import com.estapar.challenge.parking_management.dto.WebhookEventDTO;
import com.estapar.challenge.parking_management.models.Spot;
import com.estapar.challenge.parking_management.models.Ticket;
import com.estapar.challenge.parking_management.repository.SpotRepository;
import com.estapar.challenge.parking_management.repository.TicketRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class ParkedUseCaseImpl implements ParkedUseCase {

    private final TicketRepository ticketRepository;
    private final SpotRepository spotRepository;

    @Override
    public void execute(WebhookEventDTO event) {

        log.info("Processing parked event for license plate: {}", event.getLicensePlate());

        Ticket ticket = ticketRepository
                .findByLicensePlateAndExitTimeIsNull(event.getLicensePlate())
                .orElseThrow();

        Spot spot = spotRepository.findById(ticket.getSpotId())
                .orElseThrow();

        spot.setIsOccupied(true);

        spotRepository.save(spot);
    }
}