package com.office.reservation.exception;

import org.springframework.http.HttpStatus;

public class ReservationException extends Exception {

    String message;
    int errorCode;

    public ReservationException(String message) {
        super(message);
        this.message = message;
        this.errorCode=HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public ReservationException(String message, int errorCode) {
        super();
        this.message = message;
        this.errorCode = errorCode;
    }


}
