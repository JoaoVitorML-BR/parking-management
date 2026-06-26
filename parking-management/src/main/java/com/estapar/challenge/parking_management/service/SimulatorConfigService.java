package com.estapar.challenge.parking_management.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.estapar.challenge.parking_management.dto.SimulatorResponseDTO;
import com.estapar.challenge.parking_management.models.Garage;
import com.estapar.challenge.parking_management.models.Spot;
import com.estapar.challenge.parking_management.repository.GarageRepository;
import com.estapar.challenge.parking_management.repository.SpotRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulatorConfigService {
    private final RestClient restClient;
    private final GarageRepository garageRepository;
    private final SpotRepository spotRepository;

    public void fetchAndStoreSimulatorConfig() {
        log.info("Fetching simulator...");

        SimulatorResponseDTO response = restClient
                .get()
                .uri("/garage")
                .retrieve()
                .body(SimulatorResponseDTO.class);

        if (response == null) {
            log.warn("Empty response from the simulator – nothing to store.");
            return;
        }

        List<SimulatorResponseDTO.GarageDto> garageDtos = response.getGarage();
        garageDtos.forEach(dto -> {
            Garage garage = new Garage();
            garage.setSectorCode(dto.getSector());
            garage.setBasePrice(BigDecimal.valueOf(dto.getBasePrice()));
            garage.setMaxCapacity(dto.getMaxCapacity());
            garageRepository.save(garage);
            log.debug("Garage save: {}", garage);
        });

        List<SimulatorResponseDTO.SpotDto> spotDtos = response.getSpots();
        spotDtos.forEach(dto -> {
            log.info("Spot received: {}", dto);
            Spot spot = new Spot();
            spot.setId(dto.getId());

            spot.setSectorCode(dto.getSector());
            spot.setLatitude(BigDecimal.valueOf(dto.getLat()));
            spot.setLongitude(BigDecimal.valueOf(dto.getLng()));
            spot.setIsOccupied(dto.getOccupied());
            spotRepository.save(spot);
        });

        log.info("Garage configuration loaded and persisted successfully.");
    }
}
