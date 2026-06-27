package com.estapar.challenge.parking_management.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DynamicPricingServiceTest {

    private DynamicPricingService service;

    @BeforeEach
    void setUp() {
        service = new DynamicPricingService();
    }

    @Test
    void shouldApplyDiscountWhenOccupancyBelow25Percent() {
        BigDecimal factor = service.calculateFactor(2, 10); // 20%
        assertEquals(new BigDecimal("0.90"), factor);
    }

    @Test
    void shouldApplyNoDiscountWhenOccupancyBelow50Percent() {
        BigDecimal factor = service.calculateFactor(4, 10); // 40%
        assertEquals(BigDecimal.ONE, factor);
    }

    @Test
    void shouldApplySurchargeWhenOccupancyBelow75Percent() {
        BigDecimal factor = service.calculateFactor(6, 10); // 60%
        assertEquals(new BigDecimal("1.10"), factor);
    }

    @Test
    void shouldApplyMaxSurchargeWhenOccupancyBelow100Percent() {
        BigDecimal factor = service.calculateFactor(9, 10); // 90%
        assertEquals(new BigDecimal("1.25"), factor);
    }

    @Test
    void shouldReturnOneWhenMaxCapacityIsZero() {
        BigDecimal factor = service.calculateFactor(0, 0);
        assertEquals(BigDecimal.ONE, factor);
    }
}