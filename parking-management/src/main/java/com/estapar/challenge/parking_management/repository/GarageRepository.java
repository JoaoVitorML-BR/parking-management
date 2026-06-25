package com.estapar.challenge.parking_management.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.estapar.challenge.parking_management.models.Garage;

public interface GarageRepository extends JpaRepository<Garage, UUID> {
    Optional<Garage> findBySectorCode(String sectorCode);
}