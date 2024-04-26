package ru.otus.flamexander.web.server.processors;

import ru.otus.flamexander.web.server.HttpRequest;
import static ru.otus.flamexander.web.server.processors.ResponseProcessor.*;

import java.io.IOException;
import java.io.OutputStream;

public class CalculatorRequestProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        try {
            int a = Integer.parseInt(httpRequest.getParameter("a"));
            int b = Integer.parseInt(httpRequest.getParameter("b"));
            int result = a + b;
            String outMessage = a + " + " + b + " = " + result;
            responseWrite(200, "OK", "<h1>" + outMessage + "</h1>", output);
        } catch (NumberFormatException e){
            responseWrite(400, "Bad Request", "<h1>400 - Bad Request</h1>" +
                        "<p>Not valid parameters provided to request</p>", output);
        }
    }
}
