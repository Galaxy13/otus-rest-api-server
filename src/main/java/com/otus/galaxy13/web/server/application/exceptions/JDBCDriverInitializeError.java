package com.otus.galaxy13.web.server.application.exceptions;

public class JDBCDriverInitializeError extends Exception{
    public JDBCDriverInitializeError(){
        super("Error while driver initialization");
    }
}
