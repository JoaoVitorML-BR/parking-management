package com.estapar.challenge.parking_management.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; 

    @Column(name = "spot_id", nullable = true)
    private Integer spotId;
    
    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "sector_code", nullable = false)
    private String sectorCode;

    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;

    @Column(name = "exit_time", nullable = true)
    private LocalDateTime exitTime;

    @Column(name = "dynamic_price_factor", precision = 3, scale = 2, nullable = false)
    private BigDecimal dynamicPriceFactor;

    @Column(name = "amount_paid", precision = 10, scale = 2, nullable = true)
    private BigDecimal amountPaid;
}