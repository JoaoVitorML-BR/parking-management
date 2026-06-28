package com.estapar.challenge.parking_management.usecase.entry;

import com.estapar.challenge.parking_management.domain.service.DynamicPricingService;
import com.estapar.challenge.parking_management.dto.WebhookEventDTO;
import com.estapar.challenge.parking_management.exception.NoAvailableSpotException;
import com.estapar.challenge.parking_management.models.Garage;
import com.estapar.challenge.parking_management.models.Spot;
import com.estapar.challenge.parking_management.repository.GarageRepository;
import com.estapar.challenge.parking_management.repository.SpotRepository;
import com.estapar.challenge.parking_management.repository.TicketRepository;
import com.estapar.challenge.parking_management.service.SpotFinderService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntryUseCaseTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private SpotRepository spotRepository;

    @Mock
    private SpotFinderService spotFinderService;

    @Mock
    private DynamicPricingService dynamicPricingService;

    @Mock
    private GarageRepository garageRepository;

    @InjectMocks
    private EntryUseCaseImpl entryUseCase;

    private WebhookEventDTO event;
    private Spot spot;
    private Garage garage;

    @BeforeEach
    void setUp() {
        event = new WebhookEventDTO();
        event.setLicensePlate("TST0001");
        event.setEntryTime(LocalDateTime.of(2026, 6, 26, 20, 0));

        spot = new Spot();
        spot.setId(1);
        spot.setSectorCode("A");
        spot.setIsOccupied(false);

        garage = new Garage();
        garage.setSectorCode("A");
        garage.setBasePrice(new BigDecimal("40.50"));
        garage.setMaxCapacity(10);
    }

    @Test
    void shouldCreateTicketOnEntry() {

        when(spotFinderService.findSpot()).thenReturn(spot);
        when(garageRepository.findBySectorCode("A")).thenReturn(Optional.of(garage));
        when(spotRepository.countBySectorCodeAndIsOccupiedTrue("A")).thenReturn(9L);
        when(dynamicPricingService.calculateFactor(9L, 10))
                .thenReturn(new BigDecimal("1.25"));

        entryUseCase.execute(event);

        verify(dynamicPricingService)
                .calculateFactor(9L, 10);

        verify(spotRepository).save(spot);

        verify(ticketRepository).save(argThat(ticket ->
                ticket.getLicensePlate().equals("TST0001") &&
                ticket.getSectorCode().equals("A") &&
                ticket.getSpotId().equals(1) &&
                ticket.getDynamicPriceFactor().equals(new BigDecimal("1.25")) &&
                ticket.getEntryTime().equals(event.getEntryTime())
        ));

        assertTrue(spot.getIsOccupied());
    }

    @Test
    void shouldThrowExceptionWhenParkingIsFull() {

        when(spotFinderService.findSpot())
                .thenThrow(new NoAvailableSpotException("Parking lot is full."));

        assertThrows(
                NoAvailableSpotException.class,
                () -> entryUseCase.execute(event)
        );

        verify(ticketRepository, never()).save(any());
        verify(spotRepository, never()).save(any());
    }

    @Test
    void shouldUseCurrentTimeWhenEntryTimeIsNull() {

        event.setEntryTime(null);

        when(spotFinderService.findSpot()).thenReturn(spot);
        when(garageRepository.findBySectorCode("A")).thenReturn(Optional.of(garage));
        when(spotRepository.countBySectorCodeAndIsOccupiedTrue("A")).thenReturn(2L);
        when(dynamicPricingService.calculateFactor(2L, 10))
                .thenReturn(new BigDecimal("0.90"));

        entryUseCase.execute(event);

        verify(ticketRepository).save(argThat(ticket ->
                ticket.getEntryTime() != null &&
                ticket.getLicensePlate().equals("TST0001")
        ));
    }

    @Test
    void shouldThrowExceptionWhenGarageIsNotFound() {

        when(spotFinderService.findSpot()).thenReturn(spot);
        when(garageRepository.findBySectorCode("A"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> entryUseCase.execute(event)
        );

        verify(ticketRepository, never()).save(any());
    }
}