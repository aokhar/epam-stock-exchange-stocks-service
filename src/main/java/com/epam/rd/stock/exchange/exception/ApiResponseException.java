package com.epam.rd.stock.exchange.exception;

public class ApiResponseException extends RuntimeException{

    public ApiResponseException(String message) {
        super(message);
    }

}
