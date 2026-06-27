package com.estapar.challenge.parking_management.controller;

import com.estapar.challenge.parking_management.dto.RevenueRequestDTO;
import com.estapar.challenge.parking_management.dto.RevenueResponseDTO;
import com.estapar.challenge.parking_management.usecase.revenue.RevenueUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/revenue")
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueUseCase revenueUseCase;

    @GetMapping
    public ResponseEntity<RevenueResponseDTO> getRevenue(@RequestBody RevenueRequestDTO request) {
        return ResponseEntity.ok(revenueUseCase.execute(request));
    }
}