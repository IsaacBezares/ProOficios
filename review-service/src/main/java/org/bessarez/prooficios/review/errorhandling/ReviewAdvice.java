package org.bessarez.prooficios.review.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ReviewAdvice {

    @ResponseBody
    @ExceptionHandler(ReviewNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String reviewNotFoundException(ReviewNotFoundException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ReviewForOrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String reviewForOrderNotFoundException(ReviewForOrderNotFoundException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(WorkerServiceNotRespondingException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    String workerServiceNotRespondingException(WorkerServiceNotRespondingException ex){
        return ex.getMessage();
    }
}
