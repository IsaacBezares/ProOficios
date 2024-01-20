package com.example.prooficios.unavailability;

public class InvaildDateRangeException extends RuntimeException{
    InvaildDateRangeException(){
        super("Invalid date range");
    }
}
