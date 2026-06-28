package com.estapar.challenge.parking_management.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Event received from the simulator")
public class WebhookEventDTO {
    @JsonProperty("event_type")
    @Schema(description = "Type of the event", example = "ENTRY", allowableValues = {"ENTRY", "PARKED", "EXIT"})
    private String eventType;

    @JsonProperty("license_plate")
    @Schema(description = "License plate of the vehicle", example = "ZUL0001")
    private String licensePlate;

    @JsonProperty("entry_time")
    @Schema(description = "Entry time", example = "2026-06-26T23:00:00.000Z")
    private LocalDateTime entryTime;

    @JsonProperty("exit_time")
    @Schema(description = "Exit time", example = "2026-06-26T23:00:00.000Z")
    private LocalDateTime exitTime;

    @JsonProperty("lat")
    @Schema(description = "Latitude", example = "-23.550520")
    private Double lat;

    @JsonProperty("lng")
    @Schema(description = "Longitude", example = "-46.633308")
    private Double lng;
}