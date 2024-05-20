package com.otus.galaxy13.web.server.application.processors;

import com.otus.galaxy13.web.server.HttpRequest;
import com.otus.galaxy13.web.server.application.exceptions.HTTPError;
import com.otus.galaxy13.web.server.application.responses.Response;

public class RootRequestProcessor extends Processor {
    @Override
    public Response execute(HttpRequest httpRequest) throws HTTPError{
        super.logger.debug("Start page processor executed");
        super.logger.debug("Start page processor successfully responded");
        throw new HTTPError(404, httpRequest.getUri(), "Not Found", "Not Found");
    }
}
