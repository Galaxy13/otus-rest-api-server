package com.otus.galaxy13.web.server.application.processors;

import com.google.gson.Gson;
import com.otus.galaxy13.web.server.application.ddo.Item;
import com.otus.galaxy13.web.server.html.HTMLParser;
import com.otus.galaxy13.web.server.html.exceptions.HTMLParseException;
import com.otus.galaxy13.web.server.http.HTTPResponse;
import com.otus.galaxy13.web.server.http.ddo.HTTPRequest;
import com.otus.galaxy13.web.server.http.ddo.Response;
import com.otus.galaxy13.web.server.http.exceptions.HTTPError;
import com.otus.galaxy13.web.server.http.exceptions.WrongParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public abstract class Processor implements RequestProcessor{
    final Logger logger = LoggerFactory.getLogger(Processor.class);

    @Override
    public Response execute(HTTPRequest httpRequest) throws HTTPError, ClassNotFoundException {
        throw new HTTPError(404, httpRequest.getUri(), "Not Found", "Operaion not found, obsolete or not implemented");
    }

    @Override
    public Response execute(HTTPRequest httpRequest, String parameter) throws HTTPError, ClassNotFoundException {
        throw new HTTPError(404, httpRequest.getUri(), "Not Found", "Operaion not found, obsolete or not implemented");
    }

    private <T> String bodyHandlerHTML(HTTPRequest httpRequest, T obj, String templateName) throws HTTPError {
        try {
            return HTMLParser.parseHTML("templates/" + templateName, obj);
        } catch (IOException | HTMLParseException e){
            logger.warn("HTML parsing failed", e);
            throw HTTPResponse.err500(httpRequest, "Server failed to form response");
        }
    }

    private <T> String bodyHandlerHTML(HTTPRequest httpRequest, List<T> objArray, String templateName) throws HTTPError {
        try {
            return HTMLParser.parseTableHTML("templates/" + templateName, objArray);
        } catch (HTMLParseException | IOException e) {
            logger.error("HTML table parsing failed", e);
            throw HTTPResponse.err500(httpRequest, "Server failed to form multiple response");
        }
    }

    public <T> String bodyHandler(HTTPRequest httpRequest, T obj, String templateName) throws HTTPError {
        String body;
        if (httpRequest.getRequestType().equals("html")) {
            body = bodyHandlerHTML(httpRequest, obj, templateName);
        } else {
            Gson gson = new Gson();
            body = gson.toJson(obj);
        }
        return body;
    }

    public <T> String bodyHandler(HTTPRequest httpRequest, List<T> obj, String templateName) throws HTTPError {
        String body;
        if (httpRequest.getRequestType().equals("html")) {
            body = bodyHandlerHTML(httpRequest, obj, templateName);
        } else {
            Gson gson = new Gson();
            body = gson.toJson(obj);
        }
        return body;
    }

    public Item itemParser(HTTPRequest httpRequest) throws HTTPError {
        if (httpRequest.getBody().isEmpty() && httpRequest.getParameters().isEmpty()) {
            logger.debug("No JSON/query parameters passed to POST/PUT processor");
            throw HTTPResponse.err400(httpRequest, "No parameters passed");
        }
        if (!httpRequest.getBody().isEmpty() && !httpRequest.getParameters().isEmpty()) {
            logger.debug("Both JSON and query parameters passed to request");
            throw HTTPResponse.err400(httpRequest, "Using both JSON and HTTP parameters is unsupported");
        }
        logger.trace("Parameter check successful");
        Item item;
        if (httpRequest.getParameters().isEmpty()) {
            Gson gson = new Gson();
            logger.trace("Parameters to POST/PUT request parsing from JSON...");
            item = gson.fromJson(httpRequest.getBody(), Item.class);
        } else {
            try {
                logger.trace("Parameters to POST/PUT request parsing from HTTP query...");
                item = new Item(httpRequest.getParameters());
            } catch (NumberFormatException | WrongParameterException e) {
                logger.debug("Parameters to request passed with wrong format");
                throw HTTPResponse.err400(httpRequest, "Wrong parameters provided in request");
            }
        }
        if (item.getPrice() == null && item.getTitle() == null) {
            throw HTTPResponse.err400(httpRequest, "POST/PUT request passed with no parameters");
        }
        return item;
    }
}
