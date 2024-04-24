package ru.otus.flamexander.web.server.processors;

import ru.otus.flamexander.web.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

public interface RequestProcessor {
    void execute(HttpRequest httpRequest, OutputStream output) throws IOException;
}
