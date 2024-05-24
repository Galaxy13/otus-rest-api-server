package com.otus.galaxy13.web.server.application.processors;

import com.google.gson.JsonSyntaxException;
import com.otus.galaxy13.web.server.application.Storage;
import com.otus.galaxy13.web.server.application.ddo.Item;
import com.otus.galaxy13.web.server.http.HTTPResponse;
import com.otus.galaxy13.web.server.http.ddo.HTTPRequest;
import com.otus.galaxy13.web.server.http.ddo.Response;
import com.otus.galaxy13.web.server.http.exceptions.HTTPError;

import java.sql.SQLException;

public class CreateNewProductProcessor extends Processor {
    @Override
    public Response execute(HTTPRequest httpRequest) throws HTTPError {
        logger.trace("Execution of POST method to create new item");
        try {
            Item item = itemParser(httpRequest);
            logger.trace("Existing parameters retrieved from JSON/query");
            if (item.getTitle() == null || item.getPrice() == null) {
                logger.debug("Empty name/price passed with new item creation POST request");
                throw HTTPResponse.err400(httpRequest, "Title and price are required fields for new item creation");
            }
            item.setUuid();
            logger.trace("UUID set from path parameter");
            Item createdItem = Storage.save(item);
            logger.trace(String.format("New item %s created successfully", item.getTitle()));
            String body = bodyHandler(httpRequest, createdItem, "createItemTemplate.html");
            logger.trace("Response body created");
            return HTTPResponse.ok(body);
        } catch (JsonSyntaxException e) {
            logger.debug("Wrong parameters passed to create new item. {}", e.getMessage());
            throw HTTPResponse.err400(httpRequest, "Wrong parameters passed, cannot create new Item");
        } catch (SQLException e){
            logger.error("SQL exception occurred", e);
            throw HTTPResponse.err500(httpRequest, "Internal Server Error");
        } catch (ClassNotFoundException e){
            logger.error("JVM can't find JDBC driver", e);
            throw HTTPResponse.err500(httpRequest, "Storage unavailable");
        }
    }
}
