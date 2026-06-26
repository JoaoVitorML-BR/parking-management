package com.estapar.challenge.parking_management.usecase.exit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.estapar.challenge.parking_management.domain.service.ParkingPricingService;
import com.estapar.challenge.parking_management.dto.WebhookEventDTO;
import com.estapar.challenge.parking_management.models.Garage;
import com.estapar.challenge.parking_management.models.Spot;
import com.estapar.challenge.parking_management.models.Ticket;
import com.estapar.challenge.parking_management.repository.GarageRepository;
import com.estapar.challenge.parking_management.repository.SpotRepository;
import com.estapar.challenge.parking_management.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExitUseCaseImpl implements ExitUseCase {

    private final TicketRepository ticketRepository;
    private final SpotRepository spotRepository;
    private final GarageRepository garageRepository;
    private final ParkingPricingService parkingPricingService;

    @Override
    public void execute(WebhookEventDTO event) {

        Ticket ticket = ticketRepository
                .findByLicensePlateAndExitTimeIsNull(event.getLicensePlate())
                .orElseThrow(() -> new RuntimeException("Ticket not found for plate: " + event.getLicensePlate()));

        Spot spot = spotRepository.findById(ticket.getSpotId())
                .orElseThrow(() -> new RuntimeException("Spot not found: " + ticket.getSpotId()));

        Garage garage = garageRepository.findBySectorCode(ticket.getSectorCode())
                .orElseThrow(() -> new RuntimeException("Garage not found for sector: " + ticket.getSectorCode()));

        LocalDateTime exitTime = event.getExitTime() != null ? event.getExitTime() : LocalDateTime.now();
        long minutesParked = ChronoUnit.MINUTES.between(ticket.getEntryTime(), exitTime);

        BigDecimal amount = parkingPricingService.calculateAmount(ticket, garage, minutesParked);

        ticket.setExitTime(exitTime);
        ticket.setAmountPaid(amount);

        spot.setIsOccupied(false);

        spotRepository.save(spot);
        ticketRepository.save(ticket);
    }
}