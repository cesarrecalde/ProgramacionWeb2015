package com.ha.util;

import java.util.Map;

/**
 * Created by cesar on 07/10/15.
 */
public class CSVFileReadingException extends Exception{

    private String message;

    public CSVFileReadingException(){
        this.message = new String("");
    }

    public CSVFileReadingException(String m){
        this.message = m;
    }

    @Override
    public String getMessage(){
        return this.message;
    }

}
