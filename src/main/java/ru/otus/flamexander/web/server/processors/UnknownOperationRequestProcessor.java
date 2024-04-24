package ru.otus.flamexander.web.server.processors;

import ru.otus.flamexander.web.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class UnknownOperationRequestProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String response = """
        HTTP/1.1 404 Not Found\r
        Content-Type: text/html\r
        \r
        <html>
        <body>
               <h1>404 - Page Not Found</h1>
               <p>The page you are looking for might have been removed, or is temporarily unavailable</p>
        </body></html>""";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
