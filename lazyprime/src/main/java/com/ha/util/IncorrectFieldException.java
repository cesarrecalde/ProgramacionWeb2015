package com.ha.util;

/**
 * Created by cesar on 07/10/15.
 */
public class IncorrectFieldException extends Exception{


    private String message;

    public IncorrectFieldException( String message ){
        this.message = message;
    }

    @Override
    public String getMessage(){
        return this.message;
    }

}
