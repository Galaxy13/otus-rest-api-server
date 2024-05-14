package com.otus.galaxy13.web.server.application.processors;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.otus.galaxy13.web.server.HttpRequest;
import com.otus.galaxy13.web.server.application.Item;
import com.otus.galaxy13.web.server.application.Storage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import static com.otus.galaxy13.web.server.application.processors.ResponseProcessor.responseErrJson;
import static com.otus.galaxy13.web.server.application.processors.ResponseProcessor.responseJson;

public class UpdateItemProcessor extends Processor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        super.logger.trace("Item update processor executed");
        Gson gson = new Gson();
        try {
            Item item = gson.fromJson(httpRequest.getBody(), Item.class);
            if (item.getId() != null) {
                UUID updateItemUUID = item.getId();
                super.logger.trace(String.format("UUID %s found in HTTP request body", updateItemUUID));
                if (Storage.contains(updateItemUUID)) {
                    super.logger.trace(String.format("Item with UUID %s found in static storage", updateItemUUID));
                    if (item.getTitle() != null || item.getPrice() != null) {
                        super.logger.trace("PUT request is correct. Proceeding to update");
                        Storage.modifyItem(updateItemUUID, item);
                        String updatedItem = gson.toJson(Storage.getItem(updateItemUUID));
                        responseJson(200, "OK", updatedItem, output);
                        super.logger.debug("Item update processed successfully and respond to socket");
                    } else {
                        responseErrJson(gson, 400, "/items", "Bad Request", "No parameters passed to update", output);
                        super.logger.debug("No parameters passed in update request");
                    }
                } else {
                    responseErrJson(gson, 400, "/items", "Bad Request", "No item found with passed UUID", output);
                    super.logger.debug("No items exist in static storage with provided UUID");
                }
            } else {
                responseErrJson(gson, 400, "/items", "Bad Request", "No UUID item found in request", output);
                super.logger.debug("No UUID found in update request");
            }
        } catch (JsonSyntaxException e) {
            responseErrJson(gson, 400, "/items", "Bad Request", "Wrong data format passed (UUID, 'title' or 'price')", output);
            super.logger.debug("Wrong JSON format." + e.getMessage());
        }
    }
}
