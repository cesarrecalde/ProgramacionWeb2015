package com.ha.util;

/**
 * Created by cesar on 11/10/15.
 */
public class ClientNotFoundException extends Exception {
    private String message;

    public ClientNotFoundException( String message ){
        this.message = message;
    }

    @Override
    public String getMessage(){
        return this.message;
    }
}
