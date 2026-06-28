package com.estapar.challenge.parking_management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
@Schema(description = "Billing response")
public class RevenueResponseDTO {
    @Schema(description = "Total amount billed", example = "151.88")
    private BigDecimal amount;

    @Schema(description = "Currency", example = "BRL")
    private String currency;

     @Schema(description = "Timestamp of the query", example = "2026-06-26T23:00:00.000Z")
    private String timestamp;
}