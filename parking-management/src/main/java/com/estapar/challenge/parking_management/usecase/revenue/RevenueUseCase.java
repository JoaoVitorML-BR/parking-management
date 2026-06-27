package com.estapar.challenge.parking_management.usecase.revenue;

import com.estapar.challenge.parking_management.dto.RevenueRequestDTO;
import com.estapar.challenge.parking_management.dto.RevenueResponseDTO;

public interface RevenueUseCase {
    RevenueResponseDTO execute(RevenueRequestDTO request);
}