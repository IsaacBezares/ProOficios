package com.example.prooficios.workerimage;

public class WorkerImageNotFoundException extends RuntimeException{
    public WorkerImageNotFoundException(Long id){
        super("could not find WorkerImage of id " + id);
    }
}
