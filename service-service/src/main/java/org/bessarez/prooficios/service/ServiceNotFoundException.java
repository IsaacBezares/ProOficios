package org.bessarez.prooficios.service;

public class ServiceNotFoundException extends RuntimeException{
    public ServiceNotFoundException(Long id){
        super("Could not find service of id " + id);
    }
}
