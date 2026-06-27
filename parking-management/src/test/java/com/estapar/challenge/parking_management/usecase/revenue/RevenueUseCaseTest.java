package com.estapar.challenge.parking_management.usecase.revenue;

import com.estapar.challenge.parking_management.dto.RevenueRequestDTO;
import com.estapar.challenge.parking_management.dto.RevenueResponseDTO;
import com.estapar.challenge.parking_management.models.Ticket;
import com.estapar.challenge.parking_management.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RevenueUseCaseTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private RevenueUseCaseImpl revenueUseCase;

    private RevenueRequestDTO request;

    @BeforeEach
    void setUp() {
        request = new RevenueRequestDTO();
        request.setDate("2026-06-26");
        request.setSector("A");
    }

    @Test
    void shouldReturnTotalRevenueForSectorAndDate() {
        Ticket t1 = new Ticket();
        t1.setAmountPaid(new BigDecimal("151.88"));

        Ticket t2 = new Ticket();
        t2.setAmountPaid(new BigDecimal("50.63"));

        when(ticketRepository.findBySectorCodeAndExitTimeBetween(
                eq("A"), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(t1, t2));

        RevenueResponseDTO response = revenueUseCase.execute(request);

        assertEquals(new BigDecimal("202.51"), response.getAmount());
        assertEquals("BRL", response.getCurrency());
    }

    @Test
    void shouldReturnZeroWhenNoTicketsFound() {
        when(ticketRepository.findBySectorCodeAndExitTimeBetween(
                eq("A"), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        RevenueResponseDTO response = revenueUseCase.execute(request);

        assertEquals(BigDecimal.ZERO, response.getAmount());
    }

    @Test
    void shouldIgnoreTicketsWithNullAmountPaid() {
        Ticket t1 = new Ticket();
        t1.setAmountPaid(new BigDecimal("151.88"));

        Ticket t2 = new Ticket();
        t2.setAmountPaid(null);

        when(ticketRepository.findBySectorCodeAndExitTimeBetween(
                eq("A"), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(t1, t2));

        RevenueResponseDTO response = revenueUseCase.execute(request);

        assertEquals(new BigDecimal("151.88"), response.getAmount());
    }
}