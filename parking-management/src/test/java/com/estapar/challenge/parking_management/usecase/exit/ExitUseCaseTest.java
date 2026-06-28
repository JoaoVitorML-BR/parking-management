package com.estapar.challenge.parking_management.usecase.exit;

import com.estapar.challenge.parking_management.domain.service.ParkingPricingService;
import com.estapar.challenge.parking_management.dto.WebhookEventDTO;
import com.estapar.challenge.parking_management.models.Garage;
import com.estapar.challenge.parking_management.models.Spot;
import com.estapar.challenge.parking_management.models.Ticket;
import com.estapar.challenge.parking_management.repository.GarageRepository;
import com.estapar.challenge.parking_management.repository.SpotRepository;
import com.estapar.challenge.parking_management.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExitUseCaseTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private SpotRepository spotRepository;

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private ParkingPricingService parkingPricingService;

    @InjectMocks
    private ExitUseCaseImpl exitUseCase;

    private WebhookEventDTO event;
    private Ticket ticket;
    private Spot spot;
    private Garage garage;

    @BeforeEach
    void setUp() {
        event = new WebhookEventDTO();
        event.setLicensePlate("TST0001");
        event.setExitTime(LocalDateTime.of(2026, 6, 26, 23, 0, 0));

        ticket = new Ticket();
        ticket.setLicensePlate("TST0001");
        ticket.setSpotId(1);
        ticket.setSectorCode("A");
        ticket.setEntryTime(LocalDateTime.of(2026, 6, 26, 20, 0, 0));
        ticket.setDynamicPriceFactor(new BigDecimal("1.25"));

        spot = new Spot();
        spot.setId(1);
        spot.setSectorCode("A");
        spot.setIsOccupied(true);

        garage = new Garage();
        garage.setSectorCode("A");
        garage.setBasePrice(new BigDecimal("40.50"));
        garage.setMaxCapacity(10);
    }

    @Test
    void shouldCalculateAmountAndFreeSpotOnExit() {
        when(ticketRepository.findByLicensePlateAndExitTimeIsNull("TST0001"))
                .thenReturn(Optional.of(ticket));
        when(spotRepository.findById(1)).thenReturn(Optional.of(spot));
        when(garageRepository.findBySectorCode("A")).thenReturn(Optional.of(garage));
        when(parkingPricingService.calculateAmount(any(), any(), anyLong()))
                .thenReturn(new BigDecimal("151.88"));

        exitUseCase.execute(event);

        assertFalse(spot.getIsOccupied());
        assertEquals(new BigDecimal("151.88"), ticket.getAmountPaid());
        assertEquals(event.getExitTime(), ticket.getExitTime());
        verify(spotRepository).save(spot);
        verify(ticketRepository).save(ticket);
    }

    @Test
    void shouldThrowExceptionWhenTicketNotFound() {
        when(ticketRepository.findByLicensePlateAndExitTimeIsNull("TST0001"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> exitUseCase.execute(event));

        verify(spotRepository, never()).save(any());
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void shouldUseCurrentTimeWhenExitTimeIsNull() {
        event.setExitTime(null);

        when(ticketRepository.findByLicensePlateAndExitTimeIsNull("TST0001"))
                .thenReturn(Optional.of(ticket));
        when(spotRepository.findById(1)).thenReturn(Optional.of(spot));
        when(garageRepository.findBySectorCode("A")).thenReturn(Optional.of(garage));
        when(parkingPricingService.calculateAmount(any(), any(), anyLong()))
                .thenReturn(BigDecimal.ZERO);

        exitUseCase.execute(event);

        assertNotNull(ticket.getExitTime());
    }
}
