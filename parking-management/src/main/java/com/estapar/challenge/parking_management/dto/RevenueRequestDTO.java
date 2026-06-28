package com.estapar.challenge.parking_management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Billing query request")
public class RevenueRequestDTO {
    @Schema(description = "Date of the query", example = "2026-06-26")
    private String date;
    @Schema(description = "Parking sector", example = "A")
    private String sector;
}