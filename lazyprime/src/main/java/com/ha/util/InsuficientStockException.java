package com.ha.util;

/**
 * Created by cesar on 11/10/15.
 */
public class InsuficientStockException extends Exception{

    private String message;

    public InsuficientStockException( String message ){
        this.message = message;
    }

    @Override
    public String getMessage(){
        return this.message;
    }
}
