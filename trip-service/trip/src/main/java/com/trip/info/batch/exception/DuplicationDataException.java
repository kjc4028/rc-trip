package com.trip.info.batch.exception;

public class DuplicationDataException extends Exception {
    
    public DuplicationDataException(){

    }

    public DuplicationDataException(String msg){
        super(msg);
    }
}
