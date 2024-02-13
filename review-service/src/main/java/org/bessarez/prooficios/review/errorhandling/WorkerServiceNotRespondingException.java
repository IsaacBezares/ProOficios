package org.bessarez.prooficios.review.errorhandling;

public class WorkerServiceNotRespondingException extends RuntimeException{
    public WorkerServiceNotRespondingException(){
        super("Worker Service is taking longer than expected to respond." +
                " Please check back in sometime ");
    }
}
