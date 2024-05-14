package com.otus.galaxy13.web.server.application.processors;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.otus.galaxy13.web.server.HttpRequest;
import com.otus.galaxy13.web.server.application.Item;
import com.otus.galaxy13.web.server.application.Storage;

import java.io.IOException;
import java.io.OutputStream;

public class CreateNewProductProcessor extends Processor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        logger.trace("Execution of POST method to create new item");
        Gson gson = new Gson();
        try {
            Item item = gson.fromJson(httpRequest.getBody(), Item.class);
            if (item.getTitle() == null || item.getPrice() == null) {
                ResponseProcessor.responseErrJson(gson, 400, "/items", "Bad Request", "Title and price are required fields for new item creation", output);
                logger.debug("Empty name/price passed with new item creation POST request");
                return;
            }
            Storage.save(item);
            String jsonOutItem = gson.toJson(item);
            ResponseProcessor.responseJson(201, "Created", jsonOutItem, output);
            logger.trace(String.format("New item %s created successfully", item.getId()));
        } catch (JsonSyntaxException e) {
            logger.debug("Wrong parameters passed to create new item. " + e.getMessage());
            ResponseProcessor.responseErrJson(gson, 400, "/items",
                    "Bad Request",
                    "Wrong parameters passed, cannot create new Item",
                    output);
        }
    }
}
