package ru.otus.flamexander.web.server;

public class BrokenHTTPRequestException extends Exception{
    public BrokenHTTPRequestException(){
        super("Not valid HTTP request");
    }
}
