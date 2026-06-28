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
        // 31min - 30min grátis = 1min = around to 1 hour
        BigDecimal amount = service.calculateAmount(ticket, garage, 31);
        // 40.50 * 1.25 * 1 = 50.63
        assertEquals(new BigDecimal("50.63"), amount);
    }

    @Test
    void shouldChargeThreeHoursWhenParked3Hours() {
        // 180min - 30min grátis = 150min = around to 3h
        BigDecimal amount = service.calculateAmount(ticket, garage, 180);
        // 40.50 * 1.25 * 3 = 151.88
        assertEquals(new BigDecimal("151.88"), amount);
    }

    @Test
    void shouldChargeCorrectlyWithDiscountFactor() {
        ticket.setDynamicPriceFactor(new BigDecimal("0.90"));
        BigDecimal amount = service.calculateAmount(ticket, garage, 90);
        // 90min - 30min = 60min = 1h
        // 40.50 * 0.90 * 1 = 36.45
        assertEquals(new BigDecimal("36.45"), amount);
    }
}