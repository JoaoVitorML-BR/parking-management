package com.estapar.challenge.parking_management.controller;

import com.estapar.challenge.parking_management.dto.RevenueRequestDTO;
import com.estapar.challenge.parking_management.dto.RevenueResponseDTO;
import com.estapar.challenge.parking_management.usecase.revenue.RevenueUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/revenue")
@Tag(name = "Revenue", description = "Consulta de faturamento por setor e data")
public class RevenueController {

    private final RevenueUseCase revenueUseCase;

    @GetMapping
    @Operation(summary = "Consultar faturamento", description = "Retorna a receita total de um setor em uma data específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Faturamento retornado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<RevenueResponseDTO> getRevenue(
            @RequestParam @Parameter(description = "Data da consulta", example = "2026-06-26") String date,
            @RequestParam @Parameter(description = "Setor da garagem", example = "A") String sector) {

        RevenueRequestDTO request = new RevenueRequestDTO();
        request.setDate(date);
        request.setSector(sector);

        return ResponseEntity.ok(revenueUseCase.execute(request));
    }
}