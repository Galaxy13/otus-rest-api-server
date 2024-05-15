package com.otus.galaxy13.web.server;

import com.otus.galaxy13.web.server.application.exceptions.HTTPError;
import com.otus.galaxy13.web.server.application.processors.*;
import com.otus.galaxy13.web.server.application.responses.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private final Map<String, Processor> router;
    private final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    public Dispatcher() {
        logger.trace("Dispatcher init started");
        this.router = new HashMap<>();
        this.router.put("GET /", new RootRequestProcessor());
        this.router.put("GET /file", new FileRequestProcessor());
        this.router.put("GET /items", new GetProductsProcessor());
        this.router.put("POST /items", new CreateNewProductProcessor());
        this.router.put("PUT /items", new UpdateItemProcessor());
        this.router.put("DELETE /items", new DeleteItemRequestProcessor());
    }

    public void execute(HttpRequest httpRequest, OutputStream outputStream) throws IOException {
        logger.trace(String.format("Dispatcher execution requested with HTTP request: %s", httpRequest.getRouteKey()));
        if (!router.containsKey(httpRequest.getRouteKey())) {
            logger.debug(String.format("Unknown HTTP method requested: %s", httpRequest.getUri()));
            ResponseProcessor.responseErr(new HTTPError(404, httpRequest.getUri(), "Not found", "Operaion not found, obsolete or not implemented"),
                    outputStream,
                    httpRequest.getRequestType());
        }
        logger.trace(String.format("Method found in 'router', proceed to execution: %s", httpRequest.getUri()));
        try {
            Response response = router.get(httpRequest.getRouteKey()).execute(httpRequest, outputStream);
            ResponseProcessor.sendResponse(response, outputStream, httpRequest.getRequestType());
        } catch (HTTPError e){
            ResponseProcessor.responseErr(e, outputStream, httpRequest.getRequestType());
        }
    }
}
