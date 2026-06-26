package com.estapar.challenge.parking_management.usecase.parked;

import com.estapar.challenge.parking_management.dto.WebhookEventDTO;

public interface ParkedUseCase {
    void execute(WebhookEventDTO event);
}
