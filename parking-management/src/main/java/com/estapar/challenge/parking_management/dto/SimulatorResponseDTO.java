package com.estapar.challenge.parking_management.dto;

import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class SimulatorResponseDTO {
    private List<GarageDto> garage;
    private List<SpotDto> spots;

    @Data
    public static class GarageDto {
        private String sector;
        @JsonProperty("base_price")
        private Double basePrice;
        @JsonProperty("max_capacity")
        private Integer maxCapacity;
    }

    @Data
    public static class SpotDto {
        private Integer id;

        @JsonProperty("sector")
        private String sector;

        @JsonProperty("lat")
        private Double lat;

        @JsonProperty("lng")
        private Double lng;

        @JsonProperty("occupied")
        private Boolean occupied;
    }
}
