package com.estapar.challenge.parking_management.usecase.exit;

import com.estapar.challenge.parking_management.dto.WebhookEventDTO;

public interface ExitUseCase {
    void execute(WebhookEventDTO event);
}

