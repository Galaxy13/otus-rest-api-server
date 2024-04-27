package ru.otus.flamexander.web.server.application.processors;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ru.otus.flamexander.web.server.HttpRequest;
import ru.otus.flamexander.web.server.application.Item;
import ru.otus.flamexander.web.server.application.Storage;

import java.io.IOException;
import java.io.OutputStream;

import static ru.otus.flamexander.web.server.application.processors.ResponseProcessor.responseErrJson;
import static ru.otus.flamexander.web.server.application.processors.ResponseProcessor.responseJson;

public class CreateNewProductProcessor extends Processor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        logger.trace("Execution of POST method to create new item");
        Gson gson = new Gson();
        try {
            Item item = gson.fromJson(httpRequest.getBody(), Item.class);
            if (item.getTitle() == null || item.getPrice() == null) {
                responseErrJson(gson, 400, "/items", "Bad Request", "Title and price are required fields for new item creation", output);
                logger.debug("Empty name/price passed with new item creation POST request");
                return;
            }
            Storage.save(item);
            String jsonOutItem = gson.toJson(item);
            responseJson(201, "Created", jsonOutItem, output);
            logger.trace(String.format("New item %s created successfully", item.getId()));
        } catch (JsonSyntaxException e) {
            logger.debug("Wrong parameters passed to create new item. " + e.getMessage());
            responseErrJson(gson, 400, "/items",
                    "Bad Request",
                    "Wrong parameters passed, cannot create new Item",
                    output);
        }
    }
}
