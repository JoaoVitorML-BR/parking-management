package com.estapar.challenge.parking_management.exception;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(String plate) {
        super(plate);
    }
}
