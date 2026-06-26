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

    private static final int MAX_RETRIES = 30;
    private static final int RETRY_DELAY_MS = 1000;

    private final SimulatorConfigService simulatorConfigService;

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() throws InterruptedException {

        log.info("Application ready – starting garage search...");

        for (int i = 1; i <= MAX_RETRIES; i++) {
            try {
                simulatorConfigService.fetchAndStoreSimulatorConfig();
                return;
            } catch (Exception e) {
                log.info("Simulator not ready. Retry {}/{}", i, MAX_RETRIES);
                Thread.sleep(RETRY_DELAY_MS);
            }
        }

        log.error("Simulator did not become available.");
    }
}