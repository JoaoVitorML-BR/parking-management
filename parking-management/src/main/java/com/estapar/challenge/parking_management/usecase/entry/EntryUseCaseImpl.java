package com.estapar.challenge.parking_management.usecase.entry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.estapar.challenge.parking_management.domain.service.DynamicPricingService;
import com.estapar.challenge.parking_management.dto.WebhookEventDTO;
import com.estapar.challenge.parking_management.models.Garage;
import com.estapar.challenge.parking_management.models.Spot;
import com.estapar.challenge.parking_management.models.Ticket;
import com.estapar.challenge.parking_management.repository.GarageRepository;
import com.estapar.challenge.parking_management.repository.SpotRepository;
import com.estapar.challenge.parking_management.repository.TicketRepository;
import com.estapar.challenge.parking_management.service.SpotFinderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EntryUseCaseImpl implements EntryUseCase {

    private final TicketRepository ticketRepository;
    private final SpotRepository spotRepository;
    private final SpotFinderService spotFinderService;
    private final DynamicPricingService dynamicPricingService;
    private final GarageRepository garageRepository;

    @Override
    public void execute(WebhookEventDTO event) {

        Spot spot = spotFinderService.findSpot();

        spot.setIsOccupied(true);
        spotRepository.save(spot);

        Garage garage = garageRepository.findBySectorCode(spot.getSectorCode())
                .orElseThrow(() -> new RuntimeException("Garage not found for sector: " + spot.getSectorCode()));

        long occupied = spotRepository.countBySectorCodeAndIsOccupiedTrue(spot.getSectorCode());
        BigDecimal factor = dynamicPricingService.calculateFactor(occupied, garage.getMaxCapacity());

        Ticket ticket = new Ticket();
        ticket.setLicensePlate(event.getLicensePlate());
        ticket.setEntryTime(
                event.getEntryTime() != null
                        ? event.getEntryTime()
                        : LocalDateTime.now());
        ticket.setSpotId(spot.getId());
        ticket.setSectorCode(spot.getSectorCode());
        ticket.setDynamicPriceFactor(factor);

        ticketRepository.save(ticket);
    }
}