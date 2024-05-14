package com.otus.galaxy13.web.server.application.processors;

import com.otus.galaxy13.web.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

import static com.otus.galaxy13.web.server.application.processors.ResponseProcessor.responseHtml;

public class RootRequestProcessor extends Processor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        super.logger.debug("Start page processor executed");
        responseHtml(200, "OK", """
                <h1>Welcome to Simple REST API Doc</h1>
                
                <p>This API provides the following endpoints:</p>
                
                <ul>
                
                <li>GET /calc?a=value1?b=value2 - Adds two numbers</li>
                                
                <li>GET /hello - Prints 'Hello world!'</li>
                
                <li>GET /items - Returns list of all items stored (JSON)</li>
                                
                <li>POST /items - creates new item, json parameters 'title' and 'price' required. Example: {"title": "apple", "price": 100}</li>
                                
                <li>PUT /items - updates existing item, json parameters 'uuid', 'title' or 'price' required. Example: {"uuid": 1325-da43-32ce-.., "title": "carrot"}</li>
                
                </ul>""", output);
        super.logger.debug("Start page processor successfully responded");
    }
}
