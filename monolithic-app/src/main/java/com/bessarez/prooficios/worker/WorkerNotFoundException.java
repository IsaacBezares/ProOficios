package com.bessarez.prooficios.worker;

public class WorkerNotFoundException extends RuntimeException {
    public WorkerNotFoundException(Long id){
        super("could not find Worker of id " + id);
    }
}
