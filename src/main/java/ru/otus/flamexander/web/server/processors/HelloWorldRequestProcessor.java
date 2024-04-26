package ru.otus.flamexander.web.server.processors;

import ru.otus.flamexander.web.server.HttpRequest;
import static ru.otus.flamexander.web.server.processors.ResponseProcessor.*;

import java.io.IOException;
import java.io.OutputStream;

public class HelloWorldRequestProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        responseWrite(200, "OK", "<h1>Hello World!!!</h1>", output);
    }
}
