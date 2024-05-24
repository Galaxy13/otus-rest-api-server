package com.otus.galaxy13.web.server.http.exceptions;

public class BrokenHTTPRequestException extends Exception{
    public BrokenHTTPRequestException(){
        super("Not valid HTTP request");
    }
}
