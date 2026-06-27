package com.estapar.challenge.parking_management.domain.service;

import com.estapar.challenge.parking_management.models.Garage;
import com.estapar.challenge.parking_management.models.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParkingPricingServiceTest {

    private ParkingPricingService service;
    private Garage garage;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        service = new ParkingPricingService();

        garage = new Garage();
        garage.setBasePrice(new BigDecimal("40.50"));
        garage.setMaxCapacity(10);
        garage.setSectorCode("A");

        ticket = new Ticket();
        ticket.setDynamicPriceFactor(new BigDecimal("1.25"));
    }

    @Test
    void shouldReturnZeroWhenParkedWithin30Minutes() {
        BigDecimal amount = service.calculateAmount(ticket, garage, 20);
        assertEquals(BigDecimal.ZERO, amount);
    }

    @Test
    void shouldReturnZeroWhenParkedExactly30Minutes() {
        BigDecimal amount = service.calculateAmount(ticket, garage, 30);
        assertEquals(BigDecimal.ZERO, amount);
    }

    @Test
    void shouldChargeOneHourWhenParked31Minutes() {
        // 31min - 30min grátis = 1min = arredonda pra 1h
        BigDecimal amount = service.calculateAmount(ticket, garage, 31);
        // 40.50 * 1.25 * 1 = 50.63
        assertEquals(new BigDecimal("50.63"), amount);
    }
}