package com.trip.mbti.rest.common;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "set")
public class ResponseDto<D> {
    
    private HttpStatus status;
    private String message;
    private Object data;

    public static <D> ResponseDto<D> setSuccess(HttpStatus status, String message, D data) {
		return ResponseDto.set(status, message, data);
	}
	
	public static <D> ResponseDto<D> setFailed(String message) {
		return ResponseDto.set(HttpStatus.BAD_REQUEST, message, null);
	}

}
