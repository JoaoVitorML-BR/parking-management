package com.estapar.challenge.parking_management.usecase.entry;

import com.estapar.challenge.parking_management.dto.WebhookEventDTO;

public interface EntryUseCase {
    void execute(WebhookEventDTO event);
}