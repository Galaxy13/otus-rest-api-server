package ru.otus.flamexander.web.server.processors;

import ru.otus.flamexander.web.server.HttpRequest;
import static ru.otus.flamexander.web.server.processors.ResponseProcessor.*;

import java.io.IOException;
import java.io.OutputStream;

public class UnknownOperationRequestProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        responseWrite(404, "Not Found", """
               <h1>404 - Page Not Found</h1>
               <p>The page you are looking for might have been removed, or is temporarily unavailable</p>""", output);
    }
}
