package com.otus.galaxy13.web.server.application.processors;

import com.otus.galaxy13.web.server.HttpRequest;
import com.otus.galaxy13.web.server.application.exceptions.HTTPError;
import com.otus.galaxy13.web.server.application.responses.Response;

import java.io.IOException;
import java.io.OutputStream;

public interface RequestProcessor {
    Response execute(HttpRequest httpRequest, OutputStream output) throws IOException, HTTPError;
}
