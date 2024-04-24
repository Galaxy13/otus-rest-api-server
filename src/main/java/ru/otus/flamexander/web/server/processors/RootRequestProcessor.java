package ru.otus.flamexander.web.server.processors;

import ru.otus.flamexander.web.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RootRequestProcessor implements RequestProcessor{
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String response = """
        HTTP/1.1 200 OK\r
        Content-Type: text/html\r
        \r
        <html>
        <body>
        <h1>Welcome to Simple REST API Doc</h1>
             <p>This API provides the following endpoints:</p>
                 <ul>
                     <li>/calc?a=value1?b=value2 - Adds two numbers</li>
                     <li>/hello - Prints 'Hello world!'</li>
                 </ul>
        </body></html>""";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
