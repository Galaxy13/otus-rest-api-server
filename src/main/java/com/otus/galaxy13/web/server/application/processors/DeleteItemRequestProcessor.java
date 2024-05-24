package com.otus.galaxy13.web.server.application.processors;

import com.otus.galaxy13.web.server.application.Storage;
import com.otus.galaxy13.web.server.application.ddo.Item;
import com.otus.galaxy13.web.server.application.exceptions.ItemNotExists;
import com.otus.galaxy13.web.server.http.HTTPResponse;
import com.otus.galaxy13.web.server.http.ddo.HTTPRequest;
import com.otus.galaxy13.web.server.http.ddo.Response;
import com.otus.galaxy13.web.server.http.exceptions.HTTPError;

import java.sql.SQLException;
import java.util.UUID;

public class DeleteItemRequestProcessor extends Processor{
    @Override
    public Response execute(HTTPRequest httpRequest, String parameter) throws HTTPError, ClassNotFoundException {
        try {
            logger.trace("POST processor started...");
            UUID deleteUuid = UUID.fromString(parameter);
            logger.trace("UUID successfully parsed from path parameter");
            Item deletedItem = Storage.delete(deleteUuid);
            logger.debug("Item successfully removed from database");
            String body = bodyHandler(httpRequest, deletedItem, "deleteItemTemplate.html");
            logger.trace("Response body created");
            return HTTPResponse.ok(body);
        } catch (ClassCastException e) {
            logger.debug("Parameter passed to request is not UUID");
            throw HTTPResponse.err400(httpRequest, "Not UUID provided");
        } catch (SQLException e) {
            logger.error("No connection to database", e);
            throw HTTPResponse.err500(httpRequest, "Internal storage error");
        } catch (ItemNotExists e) {
            logger.trace("Item set for deletion not found");
            throw HTTPResponse.err404(httpRequest, "Item not found");
        }
    }
}
