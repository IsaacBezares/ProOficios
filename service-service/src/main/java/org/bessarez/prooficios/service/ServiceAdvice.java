package org.bessarez.prooficios.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ServiceAdvice {
    @ResponseBody
    @ExceptionHandler(ServiceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String serviceNotFoundHandler(ServiceNotFoundException ex){
        return ex.getMessage();
    }
}
