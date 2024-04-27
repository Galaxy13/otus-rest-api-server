package ru.otus.flamexander.web.server.application.processors;

import ru.otus.flamexander.web.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

import static ru.otus.flamexander.web.server.application.processors.ResponseProcessor.responseHtml;

public class HelloWorldRequestProcessor extends Processor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        super.logger.trace("Hello world processor executed");
        responseHtml(200, "OK", "<h1>Hello World!!!</h1>", output);
        super.logger.debug("Hello world processor successfully responded");
    }
}
