package org.bessarez.prooficios.review.errorhandling;

public class ReviewForOrderNotFoundException extends RuntimeException{
    public ReviewForOrderNotFoundException(Long orderId){
        super("Could not find review for order of id: " + orderId);
    }
}
