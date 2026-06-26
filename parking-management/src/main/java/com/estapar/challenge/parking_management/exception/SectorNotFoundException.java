package com.estapar.challenge.parking_management.exception;

public class SectorNotFoundException extends RuntimeException {
    public SectorNotFoundException(String sector) {
        super("Sector not found: " + sector);
    }
}
