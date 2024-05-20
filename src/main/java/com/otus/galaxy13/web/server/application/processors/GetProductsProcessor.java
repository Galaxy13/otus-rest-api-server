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
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class GetProductsProcessor extends Processor {
    @Override
    public Response execute(HttpRequest httpRequest, String parameter) throws HTTPError, ClassNotFoundException {
        super.logger.trace("Get product by id processor executed");
        Gson gson = new Gson();
        UUID itemId;
        try {
            itemId = UUID.fromString(parameter);
            logger.debug("Item id parameter retrieved from HTTP request");
        } catch (IllegalArgumentException e) {
            logger.debug("Provided parameter is not UUID (probably wrong URI)");
            throw new HTTPError(401, httpRequest.getUri(), "Not an UUID", "Wrong ID path provided (not a int number)");
        }
        Item item;
        try {
            item = Storage.getItem(itemId);
        } catch (ClassCastException | SQLException e){
            logger.debug("Error occurred while requesting item from database. Request handling halted");
            throw new HTTPError(500, httpRequest.getUri(), "Internal Server Error", "Internal Server Error");
        }
        String body;
        if (httpRequest.getRequestType().equals("html")){
            try {
                body = HTMLParser.parseHTML("getItemTemplate.html", item);
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
    public Response execute(HttpRequest httpRequest) throws HTTPError, ClassNotFoundException {
        super.logger.trace("Get all products processor executed");
        Gson gson = new Gson();
        List<Item> items;
        try {
            items = Storage.getItems();
        } catch (SQLException e){
            throw new HTTPError(500, httpRequest.getUri(), "Internal Error", "Internal Server Error");
        }
        String body;
        if (httpRequest.getRequestType().equals("json")){
            body = gson.toJson(items);
        } else {
            try {
                body = HTMLParser.parseTableHTML("getItemsTemplate.html", items);
            } catch (HTMLParseException | IOException e){
                throw new HTTPError(500, httpRequest.getUri(), "Internal Error", "Internal Server Error");
            }
        }
        return new Response(200, "OK", body);
    }
}
