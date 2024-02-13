package org.bessarez.prooficios.user;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id){
        super("could not find User of id " + id);
    }
}
