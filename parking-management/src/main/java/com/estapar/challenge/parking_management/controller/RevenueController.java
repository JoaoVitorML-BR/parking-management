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
@Tag(name = "Revenue", description = "Billing query by sector and date")
public class RevenueController {

    private final RevenueUseCase revenueUseCase;

    @GetMapping
    @Operation(summary = "Check billing ", description = "Return the billing for a given date and sector")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Billing returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<RevenueResponseDTO> getRevenue(
            @RequestParam @Parameter(description = "Date of the query", example = "2026-06-26") String date,
            @RequestParam @Parameter(description = "Parking sector", example = "A") String sector) {

        RevenueRequestDTO request = new RevenueRequestDTO();
        request.setDate(date);
        request.setSector(sector);

        return ResponseEntity.ok(revenueUseCase.execute(request));
    }
}