package com.otus.galaxy13.web.server;

import com.otus.galaxy13.web.server.application.exceptions.HTTPError;
import com.otus.galaxy13.web.server.application.exceptions.WrongParameterException;
import com.otus.galaxy13.web.server.application.processors.*;
import com.otus.galaxy13.web.server.application.responses.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class Dispatcher {
    private final Router router;
    private final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    public Dispatcher() throws WrongParameterException {
        logger.trace("Dispatcher init started");
        this.router = new Router();
        router.get("/", new RootRequestProcessor());
        router.get("/{%s}", new FileRequestProcessor());
        router.get("/items", new GetProductsProcessor());
        router.get("/items/{%d}", new GetProductsProcessor());
        router.post("/items", new CreateNewProductProcessor());
        router.put("/items/{%d}", new UpdateItemProcessor());
        router.delete("/items/{%d}", new DeleteItemRequestProcessor());
    }

    public void execute(HttpRequest httpRequest, OutputStream outputStream) throws IOException {
        logger.trace(String.format("Dispatcher execution requested with HTTP request: %s", httpRequest.getRouteKey()));
        try {
            Processor requestProcessor = router.getProcessor(httpRequest);
            Response response = requestProcessor.execute(httpRequest, outputStream);
            ResponseProcessor.sendResponse(response, outputStream, httpRequest.getRequestType());
        } catch (HTTPError e){
            ResponseProcessor.responseErr(e, outputStream, httpRequest.getRequestType());
        }
    }
}
