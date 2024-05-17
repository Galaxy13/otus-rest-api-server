package com.otus.galaxy13.web.server.application.processors;

import com.google.gson.Gson;
import com.otus.galaxy13.web.server.HTMLParser;
import com.otus.galaxy13.web.server.HttpRequest;
import com.otus.galaxy13.web.server.application.Item;
import com.otus.galaxy13.web.server.application.Storage;
import com.otus.galaxy13.web.server.application.exceptions.HTMLParseException;
import com.otus.galaxy13.web.server.application.exceptions.HTTPError;
import com.otus.galaxy13.web.server.application.responses.Response;

import java.io.IOException;

public class GetProductsProcessor extends Processor {
    @Override
    public Response execute(HttpRequest httpRequest, String parameter) throws HTTPError {
        super.logger.trace("Get product by id processor executed");
        Gson gson = new Gson();
        int itemId;
        try {
            itemId = Integer.parseInt(parameter);
        } catch (NumberFormatException e) {
            throw new HTTPError(401, httpRequest.getUri(), "Not an ID", "Wrong ID path provided (not a int number)");
        }
        Item item = Storage.getItem(itemId);
        String body;
        if (httpRequest.getRequestType().equals("html")){
            try {
                body = HTMLParser.parseHTML("/htmlTemplates", item);
            } catch (IOException | HTMLParseException e){
                logger.warn("HTML parsing failed", e);
                throw new HTTPError(500, httpRequest.getUri(), "Internal error", "Internal server error");
            }
        } else {
            body = gson.toJson(item);
        }
        return new Response(200, "OK", body);
    }

    @Override
    public Response execute(HttpRequest httpRequest) throws HTTPError {
        super.logger.trace("Get all products processor executed");
        Gson gson = new Gson();

    }
}
