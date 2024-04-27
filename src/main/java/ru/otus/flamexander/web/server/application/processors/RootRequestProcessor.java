package ru.otus.flamexander.web.server.application.processors;

import ru.otus.flamexander.web.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

import static ru.otus.flamexander.web.server.application.processors.ResponseProcessor.responseHtml;

public class RootRequestProcessor extends Processor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        super.logger.debug("Start page processor executed");
        responseHtml(200, "OK", """
                <h1>Welcome to Simple REST API Doc</h1>
                
                <p>This API provides the following endpoints:</p>
                
                <ul>
                
                <li>/calc?a=value1?b=value2 - Adds two numbers</li>
                
                <li>/hello - Prints 'Hello world!'</li>
                
                </ul>""", output);
        super.logger.debug("Start page processor successfully responded");
    }
}
