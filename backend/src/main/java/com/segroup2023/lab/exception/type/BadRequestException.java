package com.segroup2023.lab.exception.type;

public class BadRequestException extends Exception{
    private final String message;
    public BadRequestException(String message) {
        this.message = message;
    }
    public BadRequestException() {
        this.message = "";
    }

    public String getMessage() {
        return message;
    }
}
