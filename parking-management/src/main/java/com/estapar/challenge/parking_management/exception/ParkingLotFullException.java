package com.estapar.challenge.parking_management.exception;

public class ParkingLotFullException extends RuntimeException {
    public ParkingLotFullException(String message) {
        super(message);
    }
}
