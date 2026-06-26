package com.estapar.challenge.parking_management.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.estapar.challenge.parking_management.dto.WebhookEventDTO;
import com.estapar.challenge.parking_management.exception.NoAvailableSpotException;
import com.estapar.challenge.parking_management.exception.SectorNotFoundException;
import com.estapar.challenge.parking_management.exception.VehicleAlreadyParkedException;
import com.estapar.challenge.parking_management.models.Garage;
import com.estapar.challenge.parking_management.models.Spot;
import com.estapar.challenge.parking_management.models.Ticket;
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

        log.info("Processing {} event for license plate {}", eventType, plate);

        if ("ENTRY".equals(eventType)) {
            processEntry(event);
        } else {
            log.info("Event {} still not implemented, only logging", eventType);
        }
    }

    private void processEntry(WebhookEventDTO event) {
        String plate = event.getLicensePlate();
        if (ticketRepository.findByLicensePlateAndExitTimeIsNull(plate).isPresent()) {
            throw new VehicleAlreadyParkedException(plate);
        }

        Spot availableSpot = findAvailableSpot();

        String sector = availableSpot.getSectorCode();

        BigDecimal dynamicFactor = calculateDynamicPriceFactor(sector);

        log.info("Dynamic pricing factor: {}", dynamicFactor);

        availableSpot.setIsOccupied(true);

        Ticket ticket = new Ticket();
        ticket.setSpotId(availableSpot.getId());
        ticket.setLicensePlate(plate);
        ticket.setSectorCode(sector);
        ticket.setEntryTime(event.getEntryTime());
        ticket.setDynamicPriceFactor(dynamicFactor);

        spotRepository.save(availableSpot);
        ticketRepository.save(ticket);

        log.info("Ticket {} created for vehicle {}", ticket.getId(), plate);
        log.info("ENTRY processed successfully for {}", plate);
    }

    private Spot findAvailableSpot() {

        List<Garage> garages = garageRepository.findAll();

        for (Garage garage : garages) {

            String sector = garage.getSectorCode();

            long occupied = spotRepository.countBySectorCodeAndIsOccupiedTrue(sector);

            if (occupied >= garage.getMaxCapacity()) {
                continue;
            }

            var spot = spotRepository
                    .findFirstBySectorCodeAndIsOccupiedFalse(sector);

            if (spot.isPresent()) {
                log.info(
                        "Spot {} selected in sector {}",
                        spot.get().getId(),
                        sector);

                return spot.get();
            }
        }

        throw new NoAvailableSpotException("Parking lot is full.");
    }

    private BigDecimal calculateDynamicPriceFactor(String sector) {

        long maxCapacity = garageRepository.findBySectorCode(sector)
                .orElseThrow(() -> new SectorNotFoundException(sector))
                .getMaxCapacity();

        long occupiedSpots = spotRepository.countBySectorCodeAndIsOccupiedTrue(sector);

        if (maxCapacity == 0) {
            return BigDecimal.ONE;
        }

        double occupancyRate = (double) occupiedSpots / maxCapacity;

        log.info(
                "Sector {} occupancy: {}%",
                sector,
                String.format("%.2f", occupancyRate * 100));

        if (occupancyRate < 0.25) {
            return new BigDecimal("0.90");
        }

        if (occupancyRate < 0.50) {
            return BigDecimal.ONE;
        }

        if (occupancyRate < 0.75) {
            return new BigDecimal("1.10");
        }

        return new BigDecimal("1.25");
    }
}