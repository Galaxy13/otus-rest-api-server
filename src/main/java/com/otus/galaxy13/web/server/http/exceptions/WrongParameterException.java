package com.otus.galaxy13.web.server.http.exceptions;

public class WrongParameterException extends Exception {
    public WrongParameterException() {
        super("Wrong parameter passed");
    }
}
