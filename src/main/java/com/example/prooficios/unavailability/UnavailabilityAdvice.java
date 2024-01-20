package com.example.prooficios.unavailability;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UnavailabilityAdvice {

    @ResponseBody
    @ExceptionHandler(UnavailabilityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String unavailabilityNotFoundHandler(UnavailabilityNotFoundException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(InvaildDateRangeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String invalidDateRangeHandler(InvaildDateRangeException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(AvailableDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String availableDateException(AvailableDateException ex){
        return ex.getMessage();
    }
}
