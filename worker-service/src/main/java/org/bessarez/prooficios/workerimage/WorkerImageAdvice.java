package org.bessarez.prooficios.workerimage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WorkerImageAdvice {

    @ResponseBody
    @ExceptionHandler(WorkerImageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String workerImageNotFoundException(WorkerImageNotFoundException ex){
        return ex.getMessage();
    }

}