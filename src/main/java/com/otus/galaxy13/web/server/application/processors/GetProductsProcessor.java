package com.otus.galaxy13.web.server.application.processors;

import com.otus.galaxy13.web.server.application.Storage;
import com.otus.galaxy13.web.server.application.ddo.Item;
import com.otus.galaxy13.web.server.application.exceptions.ItemNotExists;
import com.otus.galaxy13.web.server.http.HTTPResponse;
import com.otus.galaxy13.web.server.http.ddo.HTTPRequest;
import com.otus.galaxy13.web.server.http.ddo.Response;
import com.otus.galaxy13.web.server.http.exceptions.HTTPError;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class GetProductsProcessor extends Processor {
    @Override
    public Response execute(HTTPRequest httpRequest, String parameter) throws HTTPError, ClassNotFoundException {
        super.logger.trace("GET product by id processor executed...");
        UUID itemId;
        try {
            itemId = UUID.fromString(parameter);
            logger.trace("Item id parameter retrieved from HTTP request");
        } catch (IllegalArgumentException e) {
            logger.debug("Provided parameter is not UUID (probably wrong URI)");
            throw HTTPResponse.err404(httpRequest, "Wrong ID path provided (not a int number)");
        }
        Item item;
        try {
            item = Storage.getItem(itemId);
        } catch (ClassCastException | SQLException e){
            logger.debug("Error occurred while requesting item from database. Request handling halted");
            throw HTTPResponse.err500(httpRequest, "Internal Server Error");
        } catch (ItemNotExists e) {
            throw HTTPResponse.err404(httpRequest, "Item not exists");
        }
        logger.trace("Item successfully received from database");
        String body = bodyHandler(httpRequest, item, "getItemTemplate.html");
        logger.trace("Response body of single item created");
        return HTTPResponse.ok(body);
    }

    @Override
    public Response execute(HTTPRequest httpRequest) throws HTTPError, ClassNotFoundException {
        super.logger.trace("GET all products processor executed...");
        List<Item> items;
        try {
            items = Storage.getItems();
        } catch (SQLException e){
            logger.debug("GET Processor catches SQL exception", e);
            throw HTTPResponse.err500(httpRequest, "Internal Storage Error");
        }
        String body = bodyHandler(httpRequest, items, "getItemsTemplate.html");
        logger.trace("Response body of multiple items created");
        return HTTPResponse.ok(body);
    }
}
