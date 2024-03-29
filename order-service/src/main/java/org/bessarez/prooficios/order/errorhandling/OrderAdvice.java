package org.bessarez.prooficios.order.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class OrderAdvice {

    @ResponseBody
    @ExceptionHandler(OrderNotfoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String orderNotfoundException(OrderNotfoundException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ReviewServiceNotRespondingException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    String reviewServiceNotRespondingException(ReviewServiceNotRespondingException ex){
        return ex.getMessage();
    }
}
