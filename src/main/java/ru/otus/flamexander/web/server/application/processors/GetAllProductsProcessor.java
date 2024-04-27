package ru.otus.flamexander.web.server.application.processors;

import com.google.gson.Gson;
import ru.otus.flamexander.web.server.HttpRequest;
import ru.otus.flamexander.web.server.application.Item;
import ru.otus.flamexander.web.server.application.Storage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static ru.otus.flamexander.web.server.application.processors.ResponseProcessor.responseJson;

public class GetAllProductsProcessor extends Processor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        super.logger.trace("Get all products processor executed");
        List<Item> items = Storage.getItems();
        Gson gson = new Gson();
        responseJson(200, "OK", gson.toJson(items), output);
        super.logger.debug("Get all products processor successfully responded");
    }
}
