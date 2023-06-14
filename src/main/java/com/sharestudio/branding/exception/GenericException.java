package com.sharestudio.branding.exception;

import org.springframework.http.HttpStatus;

public class GenericException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public HttpStatus httpStatus;
    public GenericException(String msg) {
        super(msg);
    }
    public GenericException(HttpStatus httpStatus ,String msg) {
        super(msg);
        this.httpStatus = httpStatus;
    }
}
