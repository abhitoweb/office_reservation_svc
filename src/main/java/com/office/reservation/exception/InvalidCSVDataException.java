package com.office.reservation.exception;

import org.springframework.http.HttpStatus;

public class InvalidCSVDataException extends Exception {

    String message;
    int errorCode;

    public InvalidCSVDataException(String message) {
        super();
        this.message = message;
        this.errorCode=HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public InvalidCSVDataException(String message, int errorCode) {
        super();
        this.message = message;
        this.errorCode = errorCode;
    }


}
