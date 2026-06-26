package com.estapar.challenge.parking_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estapar.challenge.parking_management.models.Spot;

public interface SpotRepository extends JpaRepository<Spot, Integer> {
    List<Spot> findBySectorCode(String sectorCode);

    Optional<Spot> findById(Integer id);

    long countBySectorCode(String sectorCode);

    long countBySectorCodeAndOccupied(String sectorCode, boolean occupied);
}
