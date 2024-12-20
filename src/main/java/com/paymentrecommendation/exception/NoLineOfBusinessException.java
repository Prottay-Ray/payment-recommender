package com.paymentrecommendation.exception;

public class NoLineOfBusinessException extends RuntimeException {

    public NoLineOfBusinessException() {
        super("The line of business is not supported");
    }
}
