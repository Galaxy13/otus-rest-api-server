package ru.otus.flamexander.web.server.processors;

import ru.otus.flamexander.web.server.HttpRequest;
import static ru.otus.flamexander.web.server.processors.ResponseProcessor.*;

import java.io.IOException;
import java.io.OutputStream;

public class RootRequestProcessor implements RequestProcessor{
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        responseWrite(200, "OK", """
                <h1>Welcome to Simple REST API Doc</h1>
                
                <p>This API provides the following endpoints:</p>
                
                <ul>
                
                <li>/calc?a=value1?b=value2 - Adds two numbers</li>
                
                <li>/hello - Prints 'Hello world!'</li>
                
                </ul>""", output);
    }
}
