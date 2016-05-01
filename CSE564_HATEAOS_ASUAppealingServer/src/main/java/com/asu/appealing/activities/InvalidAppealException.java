package com.asu.appealing.activities;

public class InvalidAppealException extends RuntimeException {
    public InvalidAppealException(Exception ex) {
        super(ex);
    }

    public InvalidAppealException() {}

    private static final long serialVersionUID = 5513552354528785774L;

}
