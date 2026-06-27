package com.estapar.challenge.parking_management.domain.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class DynamicPricingService {

    public BigDecimal calculateFactor(long occupied, long maxCapacity) {

        if (maxCapacity == 0)
            return BigDecimal.ONE;

        double rate = (double) occupied / maxCapacity;

        if (rate < 0.25)
            return new BigDecimal("0.90");
        if (rate < 0.50)
            return BigDecimal.ONE;
        if (rate < 0.75)
            return new BigDecimal("1.10");

        return new BigDecimal("1.25");
    }
}