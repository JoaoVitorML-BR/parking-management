package com.estapar.challenge.parking_management.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.estapar.challenge.parking_management.service.SimulatorConfigService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupConfig {
    private final SimulatorConfigService simulatorConfigService;

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        try {
            log.info("Aplicação pronta – iniciando fetch da garagem …");
            log.info("aguardando 10 segundos para garantir que o simulador esteja pronto …");
            Thread.sleep(10000);
            simulatorConfigService.fetchAndStoreSimulatorConfig();
        } catch (InterruptedException e) {
            log.error("Error during startup configuration: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }
}
