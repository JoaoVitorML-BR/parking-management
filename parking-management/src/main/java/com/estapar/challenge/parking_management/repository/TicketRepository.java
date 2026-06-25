package com.estapar.challenge.parking_management.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estapar.challenge.parking_management.models.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

}
