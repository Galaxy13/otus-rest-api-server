package com.otus.galaxy13.web.server.http.exceptions;

public class HTTPError extends Throwable{
    int statusCode;
    String pointer;
    String title;
    String detail;
    public HTTPError(int statusCode, String pointer, String title, String detail) {
        this.statusCode = statusCode;
        this.pointer = pointer;
        this.title = title;
        this.detail = detail;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getPointer() {
        return pointer;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }
}
