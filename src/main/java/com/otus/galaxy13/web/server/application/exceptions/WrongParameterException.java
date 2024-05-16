package com.otus.galaxy13.web.server.application.exceptions;

public class WrongParameterException extends Exception{
    public WrongParameterException(String parameter){
        super("Wrong parameter: " + parameter);
    }
}
