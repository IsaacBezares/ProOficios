package org.bessarez.prooficios.unavailability;

public class AvailableDateException extends RuntimeException{
    public AvailableDateException(){
        super("Can not unbook available date");
    }
}
