package com.estapar.challenge.parking_management.dto;

import lombok.Data;
import java.util.List;

@Data
public class SimulatorResponseDTO {
    private List<GarageDto> garage;
    private List<SpotDto> spots;

    @Data
    public static class GarageDto {
        private String sector;
        private Double basePrice;
        private Integer maxCapacity;
    }

    @Data
    public static class SpotDto {
        private Integer id;
        private String sector;
        private Double lat;
        private Double lng;
    }
}
