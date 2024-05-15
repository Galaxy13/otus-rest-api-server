package com.otus.galaxy13.web.server.application.processors;

import com.google.gson.Gson;
import com.otus.galaxy13.web.server.HttpRequest;
import com.otus.galaxy13.web.server.application.Item;
import com.otus.galaxy13.web.server.application.Storage;
import com.otus.galaxy13.web.server.application.exceptions.HTTPError;
import com.otus.galaxy13.web.server.application.responses.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class GetProductsProcessor extends Processor {
    @Override
    public Response execute(HttpRequest httpRequest, OutputStream output, boolean isHTML) throws IOException, HTTPError {
        super.logger.trace("Get all products processor executed");
        Gson gson = new Gson();
        Response response;
        int itemId;
        try {
            itemId = idRetriever(httpRequest);
        } catch (NumberFormatException e) {
            throw new HTTPError(401, httpRequest.getUri(), "Not an ID", "Wrong ID path provided (not a int number)");
        }
        if (itemId == -1) {
            List<Item> items = Storage.getItems();
            response = new Response(200, "OK", gson.toJson(items));
            super.logger.debug("Get all products processor successfully responded");
        } else {

        }
        return response;
    }
}
