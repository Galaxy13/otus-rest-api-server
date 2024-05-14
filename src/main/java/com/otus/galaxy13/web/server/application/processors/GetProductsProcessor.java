package com.otus.galaxy13.web.server.application.processors;

import com.google.gson.Gson;
import com.otus.galaxy13.web.server.HttpRequest;
import com.otus.galaxy13.web.server.application.Item;
import com.otus.galaxy13.web.server.application.Storage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class GetProductsProcessor extends Processor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        super.logger.trace("Get all products processor executed");
        List<Item> items = Storage.getItems();
        Gson gson = new Gson();
        ResponseProcessor.responseJson(200, "OK", gson.toJson(items), output);
        super.logger.debug("Get all products processor successfully responded");
    }
}
