package com.estapar.challenge.parking_management.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WebhookEventDTO {
    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("license_plate")
    private String licensePlate;

    @JsonProperty("entry_time")
    private LocalDateTime entryTime;

    @JsonProperty("exit_time")
    private LocalDateTime exitTime;

    @JsonProperty("lat")
    private Double lat;

    @JsonProperty("lng")
    private Double lng;
}