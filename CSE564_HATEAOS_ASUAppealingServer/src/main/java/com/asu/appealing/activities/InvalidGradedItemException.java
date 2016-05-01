package com.asu.appealing.activities;

public class InvalidGradedItemException extends RuntimeException {

    public InvalidGradedItemException(Exception ex) {
        super(ex);
    }

    public InvalidGradedItemException() {}

    private static final long serialVersionUID = 2124071104638224535L;

}
