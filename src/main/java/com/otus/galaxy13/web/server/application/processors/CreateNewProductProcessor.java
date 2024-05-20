package com.otus.galaxy13.web.server.application.processors;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.otus.galaxy13.web.server.HttpRequest;
import com.otus.galaxy13.web.server.application.Item;
import com.otus.galaxy13.web.server.application.Storage;
import com.otus.galaxy13.web.server.application.exceptions.HTTPError;
import com.otus.galaxy13.web.server.application.responses.Response;


import java.sql.SQLException;

public class CreateNewProductProcessor extends Processor {
    @Override
    public Response execute(HttpRequest httpRequest) throws HTTPError {
        logger.trace("Execution of POST method to create new item");
        Gson gson = new Gson();
        try {
            Item item = gson.fromJson(httpRequest.getBody(), Item.class);
            if (item.getTitle() == null || item.getPrice() == null) {
                logger.debug("Empty name/price passed with new item creation POST request");
                throw new HTTPError(400, httpRequest.getUri(), "Bad Request", "Title and price are required fields for new item creation");
            }
            Item createdItem = Storage.save(item);
            logger.trace(String.format("New item %s created successfully", item.getTitle()));
            String body;
            if (httpRequest.getRequestType().equals("html")){
                body = bodyHandlerHtml(httpRequest, createdItem, "createItemTemplate.html");
            } else {
                body = gson.toJson(createdItem);
            }
            return new Response(200, "OK", body);
        } catch (JsonSyntaxException e) {
            logger.debug("Wrong parameters passed to create new item. " + e.getMessage());
            throw new HTTPError(400, httpRequest.getUri(), "Bad Request", "Wrong parameters passed, cannot create new Item");
        } catch (SQLException e){
            logger.error("SQL exception occurred", e);
            throw new HTTPError(500, httpRequest.getUri(), "Internal Error", "Internal Server Error");
        } catch (ClassNotFoundException e){
            logger.error("JVM can't find JDBC driver", e);
            throw new HTTPError(500, httpRequest.getUri(), "Internal Error", "Storage unavailable");
        }
    }
}
