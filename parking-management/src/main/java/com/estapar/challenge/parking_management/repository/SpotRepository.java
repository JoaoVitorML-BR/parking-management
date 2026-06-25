package com.estapar.challenge.parking_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estapar.challenge.parking_management.models.Spot;

public interface SpotRepository extends JpaRepository<Spot, Integer> {
    
}
