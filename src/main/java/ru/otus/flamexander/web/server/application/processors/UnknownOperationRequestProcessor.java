package ru.otus.flamexander.web.server.application.processors;

import ru.otus.flamexander.web.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

import static ru.otus.flamexander.web.server.application.processors.ResponseProcessor.responseHtml;

public class UnknownOperationRequestProcessor extends Processor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        super.logger.trace("Unknown operation processor executed");
        responseHtml(404, "Not Found", """
               <h1>404 - Page Not Found</h1>
               <p>The page you are looking for might have been removed, or is temporarily unavailable</p>""", output);
        super.logger.debug("Unknown operation processor responded successfully");
    }
}
