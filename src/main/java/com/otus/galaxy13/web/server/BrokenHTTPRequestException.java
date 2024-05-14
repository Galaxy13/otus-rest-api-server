package com.otus.galaxy13.web.server;

public class BrokenHTTPRequestException extends Exception{
    public BrokenHTTPRequestException(){
        super("Not valid HTTP request");
    }
}
