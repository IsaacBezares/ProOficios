package com.example.prooficios.unavailability;

public class UnavailabilityNotFoundException extends RuntimeException{
    public UnavailabilityNotFoundException(Long id){
        super("Could not find unavailability of id " + id);
    }
}
