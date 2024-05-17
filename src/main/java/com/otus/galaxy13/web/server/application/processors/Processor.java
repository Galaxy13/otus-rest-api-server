package com.otus.galaxy13.web.server.application.processors;

import com.otus.galaxy13.web.server.HttpRequest;
import com.otus.galaxy13.web.server.application.exceptions.HTTPError;
import com.otus.galaxy13.web.server.application.responses.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Processor implements RequestProcessor{
    final Logger logger = LoggerFactory.getLogger(Processor.class);

    @Override
    public Response execute(HttpRequest httpRequest) throws HTTPError {
        throw new HTTPError(404, httpRequest.getUri(), "Not Found", "Operaion not found, obsolete or not implemented");
    }

    @Override
    public Response execute(HttpRequest httpRequest, String parameter) throws HTTPError{
        throw new HTTPError(404, httpRequest.getUri(), "Not Found", "Operaion not found, obsolete or not implemented");
    }
}
