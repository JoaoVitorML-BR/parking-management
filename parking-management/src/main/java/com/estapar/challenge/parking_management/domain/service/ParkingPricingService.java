package com.estapar.challenge.parking_management.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.estapar.challenge.parking_management.models.Garage;
import com.estapar.challenge.parking_management.models.Ticket;

@Service
public class ParkingPricingService {

    private static final int FREE_MINUTES = 30;

    public BigDecimal calculateAmount(Ticket ticket, Garage garage, long minutesParked) {

        if (minutesParked <= FREE_MINUTES) {
            return BigDecimal.ZERO;
        }

        long billableMinutes = minutesParked - FREE_MINUTES;
        long hours = (long) Math.ceil(billableMinutes / 60.0);

        return garage.getBasePrice()
                .multiply(ticket.getDynamicPriceFactor())
                .multiply(BigDecimal.valueOf(hours))
                .setScale(2, RoundingMode.HALF_UP);
    }
}