package com.otus.galaxy13.web.server.application.responses;

import com.otus.galaxy13.web.server.application.exceptions.HTTPError;

public class Response {
    int statusCode;
    String codeDescription;
    String body;

    public Response(int statusCode, String codeDescription, String body) {
        this.statusCode = statusCode;
        this.codeDescription = codeDescription;
        this.body = body;
    }

    public Response(HTTPError httpError, String body){
        this.statusCode = httpError.getStatusCode();
        this.codeDescription = httpError.getTitle();
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getCodeDescription() {
        return codeDescription;
    }

    public String getBody() {
        return body;
    }
}
