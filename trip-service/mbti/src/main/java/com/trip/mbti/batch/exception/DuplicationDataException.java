package com.trip.mbti.batch.exception;

public class DuplicationDataException extends Exception {
    
    public DuplicationDataException(){

    }

    public DuplicationDataException(String msg){
        super(msg);
    }
}
