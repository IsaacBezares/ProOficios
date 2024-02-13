package org.bessarez.prooficios.order.errorhandling;

public class ReviewServiceNotRespondingException extends RuntimeException{
    public ReviewServiceNotRespondingException(){
        super("Review Service is taking longer than expected to respond." +
                " Please check back in sometime ");
    }
}
