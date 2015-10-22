package com.ha.util;

/**
 * Created by cesar on 08/10/15.
 */
public class ProductNotFoundException extends Exception{

    private String message;

    public ProductNotFoundException( String message ){
        this.message = message;
    }

    @Override
    public String getMessage(){
        return this.message;
    }

}
