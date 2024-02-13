package org.bessarez.prooficios.review.errorhandling;

public class ReviewNotFoundException extends RuntimeException{
    public ReviewNotFoundException(Long id){
        super("Could not find review of id " + id);
    }
}
