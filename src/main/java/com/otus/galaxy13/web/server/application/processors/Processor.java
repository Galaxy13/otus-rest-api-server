package com.otus.galaxy13.web.server.application.processors;

import com.otus.galaxy13.web.server.HTMLParser;
import com.otus.galaxy13.web.server.HttpRequest;
import com.otus.galaxy13.web.server.application.exceptions.HTMLParseException;
import com.otus.galaxy13.web.server.application.exceptions.HTTPError;
import com.otus.galaxy13.web.server.application.responses.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class Processor implements RequestProcessor{
    final Logger logger = LoggerFactory.getLogger(Processor.class);

    @Override
    public Response execute(HttpRequest httpRequest) throws HTTPError, ClassNotFoundException {
        throw new HTTPError(404, httpRequest.getUri(), "Not Found", "Operaion not found, obsolete or not implemented");
    }

    @Override
    public Response execute(HttpRequest httpRequest, String parameter) throws HTTPError, ClassNotFoundException{
        throw new HTTPError(404, httpRequest.getUri(), "Not Found", "Operaion not found, obsolete or not implemented");
    }

    public <T> String bodyHandlerHtml(HttpRequest httpRequest, T obj, String templateName) throws HTTPError{
        try {
            return HTMLParser.parseHTML("templates/" + templateName, obj);
        } catch (IOException | HTMLParseException e){
            logger.warn("HTML parsing failed", e);
            throw new HTTPError(500, httpRequest.getUri(), "Internal error", "Internal server error");
            }
    }

}
