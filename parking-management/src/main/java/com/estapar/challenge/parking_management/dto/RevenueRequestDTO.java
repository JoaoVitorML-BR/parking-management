package com.estapar.challenge.parking_management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Requisição de consulta de faturamento")
public class RevenueRequestDTO {
    @Schema(description = "Data de consulta", example = "2026-06-26")
    private String date;
    @Schema(description = "Setor de consulta", example = "A")
    private String sector;
}