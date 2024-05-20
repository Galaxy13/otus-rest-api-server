package com.otus.galaxy13.web.server;

import com.otus.galaxy13.web.server.application.exceptions.HTTPError;
import com.otus.galaxy13.web.server.application.processors.*;
import com.otus.galaxy13.web.server.application.responses.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class Dispatcher {
    private final Router router;
    private final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    public Dispatcher(){
        logger.trace("Dispatcher init started");
        this.router = new Router();
        router.get("/", new RootRequestProcessor());
        router.get("/{filename}", new FileRequestProcessor());
        router.get("/items", new GetProductsProcessor());
        router.get("/items/{item_id}", new GetProductsProcessor());
        router.post("/items", new CreateNewProductProcessor());
        router.put("/items/{item_id}", new UpdateItemProcessor());
        router.delete("/items/{item_id}", new DeleteItemRequestProcessor());
    }

    public void execute(HttpRequest httpRequest, OutputStream outputStream) throws IOException {
        logger.trace(String.format("Dispatcher execution requested with HTTP request: %s", httpRequest.getRouteKey()));
        try {
            Response response = router.parseRequest(httpRequest);
            ResponseProcessor.sendResponse(response, outputStream, httpRequest.getRequestType());
        } catch (HTTPError e){
            ResponseProcessor.responseErr(e, outputStream, httpRequest.getRequestType());
        } catch (ClassNotFoundException e){
            throw new IOException(e);
        }
    }
}
