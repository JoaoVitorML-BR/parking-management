package com.estapar.challenge.parking_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.estapar.challenge.parking_management.exception.NoAvailableSpotException;
import com.estapar.challenge.parking_management.models.Garage;
import com.estapar.challenge.parking_management.models.Spot;
import com.estapar.challenge.parking_management.repository.GarageRepository;
import com.estapar.challenge.parking_management.repository.SpotRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpotFinderService {

    private final SpotRepository spotRepository;
    private final GarageRepository garageRepository;

    public Spot findSpot() {

        List<Garage> garages = garageRepository.findAll();

        for (Garage garage : garages) {

            long occupied = spotRepository
                    .countBySectorCodeAndIsOccupiedTrue(garage.getSectorCode());

            if (occupied >= garage.getMaxCapacity())
                continue;

            return spotRepository
                    .findFirstBySectorCodeAndIsOccupiedFalse(garage.getSectorCode())
                    .orElse(null);
        }

        throw new NoAvailableSpotException("Parking lot is full.");
    }
}