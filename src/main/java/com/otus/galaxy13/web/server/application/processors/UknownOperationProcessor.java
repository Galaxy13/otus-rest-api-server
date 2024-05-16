package com.otus.galaxy13.web.server.application.processors;

import com.otus.galaxy13.web.server.HttpRequest;
import com.otus.galaxy13.web.server.application.exceptions.HTTPError;
import com.otus.galaxy13.web.server.application.responses.Response;

import java.io.IOException;
import java.io.OutputStream;

public class UknownOperationProcessor extends Processor {

    @Override
    public Response execute(HttpRequest httpRequest, OutputStream output) throws IOException, HTTPError {
        throw new HTTPError(401, httpRequest.getUri(), "Bad Request", "No id specified for modification");
    }
}
