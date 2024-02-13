package org.bessarez.prooficios.order.errorhandling;

public class OrderNotfoundException extends RuntimeException{
    public OrderNotfoundException(Long id){
        super("could not find Order of id " + id);
    }
}
