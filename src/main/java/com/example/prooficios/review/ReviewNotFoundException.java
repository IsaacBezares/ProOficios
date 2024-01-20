package com.example.prooficios.review;

public class ReviewNotFoundException extends RuntimeException{
    public ReviewNotFoundException(Long id){
        super("Could not find review of id " + id);
    }
}
