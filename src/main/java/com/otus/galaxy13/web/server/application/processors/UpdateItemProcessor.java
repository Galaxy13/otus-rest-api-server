package com.otus.galaxy13.web.server.application.processors;

import com.otus.galaxy13.web.server.application.Storage;
import com.otus.galaxy13.web.server.application.ddo.Item;
import com.otus.galaxy13.web.server.application.exceptions.ItemNotExists;
import com.otus.galaxy13.web.server.application.exceptions.MergeException;
import com.otus.galaxy13.web.server.http.HTTPResponse;
import com.otus.galaxy13.web.server.http.ddo.HTTPRequest;
import com.otus.galaxy13.web.server.http.ddo.Response;
import com.otus.galaxy13.web.server.http.exceptions.HTTPError;

import java.sql.SQLException;
import java.util.UUID;

public class UpdateItemProcessor extends Processor {
    @Override
    public Response execute(HTTPRequest httpRequest, String parameter) throws HTTPError, ClassNotFoundException {
        super.logger.trace("Item update processor executed");
        try {
            Item item = itemParser(httpRequest);
            logger.trace("Item object created from JSON parameters");
            UUID updateItemUUID = UUID.fromString(parameter);
            logger.trace(String.format("UUID %s found in HTTP request body", updateItemUUID));
            Item currentItem = Storage.getItem(updateItemUUID);
            item.setUuid(updateItemUUID);
            currentItem.merge(item);
            super.logger.trace(String.format("Item with UUID %s found in database and merged with new item", updateItemUUID));
            Storage.update(currentItem);
            super.logger.debug("Item update processed successfully and respond to socket");
            String body = bodyHandler(httpRequest, currentItem, "updateItemTemplate.html");
            logger.trace("Response body created");
            return new Response(200, "OK", body);
        } catch (SQLException e) {
            logger.debug("SQL Exception occurred", e);
            throw HTTPResponse.err500(httpRequest, "Internal storage error");
        } catch (MergeException e) {
            logger.debug("Trying to merge items with different IDs", e);
            throw HTTPResponse.err500(httpRequest, "Internal server error");
        } catch (ItemNotExists e) {
            logger.trace("Item not exists in storage");
            throw HTTPResponse.err404(httpRequest, "Item not exists");
        }
    }
}
