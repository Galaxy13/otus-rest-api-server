package com.otus.galaxy13.web.server;

import com.otus.galaxy13.web.server.application.processors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private final Map<String, Processor> router;
    private final Processor unknownOperationRequestProcessor;
    private final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    public Dispatcher() {
        logger.trace("Dispatcher init started");
        this.router = new HashMap<>();
        this.router.put("GET /", new RootRequestProcessor());
        this.router.put("GET /items", new GetProductsProcessor());
        this.router.put("POST /items", new CreateNewProductProcessor());
        this.router.put("PUT /items", new UpdateItemProcessor());
        this.router.put("DELETE /items", new DeleteItemRequestProcessor());
        this.unknownOperationRequestProcessor = new UnknownOperationRequestProcessor();
    }

    public void execute(HttpRequest httpRequest, OutputStream outputStream) throws IOException {
        logger.trace(String.format("Dispatcher execution requested with HTTP request: %s", httpRequest.getRouteKey()));
        if (!router.containsKey(httpRequest.getRouteKey())) {
            logger.debug(String.format("Unknown HTTP method requested: %s", httpRequest.getUri()));
            unknownOperationRequestProcessor.execute(httpRequest, outputStream);
            return;
        }
        logger.trace(String.format("Method found in 'router', proceed to execution: %s", httpRequest.getUri()));
        router.get(httpRequest.getRouteKey()).execute(httpRequest, outputStream);
    }
}
