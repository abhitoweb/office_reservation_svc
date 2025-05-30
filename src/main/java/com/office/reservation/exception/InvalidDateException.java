package com.office.reservation.exception;

import org.springframework.http.HttpStatus;

public class InvalidDateException extends Exception {

	String message;
	int errorCode;

	public InvalidDateException(String message) {
		super(message);
		this.message = message;
		this.errorCode= HttpStatus.BAD_REQUEST.value();
	}
	
	
	
	
}
