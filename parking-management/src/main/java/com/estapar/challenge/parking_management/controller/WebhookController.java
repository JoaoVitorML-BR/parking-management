package com.estapar.challenge.parking_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estapar.challenge.parking_management.dto.WebhookEventDTO;
import com.estapar.challenge.parking_management.service.EventProcessorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/webhook")
@Tag(name = "Webhook", description = "Recebe eventos do simulador de garagem")
public class WebhookController {
    private final EventProcessorService eventProcessor;

    @PostMapping
    @Operation(summary = "Receber evento", description = "Aceita eventos ENTRY, PARKED e EXIT do simulador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento processado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao processar evento")
    })
    public ResponseEntity<Void> receiveWebhook(@RequestBody WebhookEventDTO event) {
        try {
            eventProcessor.process(event);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error processing webhook", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
