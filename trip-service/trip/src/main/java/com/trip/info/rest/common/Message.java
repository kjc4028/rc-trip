package com.trip.info.rest.common;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class Message {
    
    private HttpStatus status;
    private String message;
    private Object data;

    public Message(){
        this.status = HttpStatus.BAD_REQUEST;
        this.message = null;
        this.data = null;
    }
}
