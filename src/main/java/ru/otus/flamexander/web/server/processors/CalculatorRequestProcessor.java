package ru.otus.flamexander.web.server.processors;

import ru.otus.flamexander.web.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CalculatorRequestProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String response;
        try {
            int a = Integer.parseInt(httpRequest.getParameter("a"));
            int b = Integer.parseInt(httpRequest.getParameter("b"));
            int result = a + b;
            String outMessage = a + " + " + b + " = " + result;
            response = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<html><body><h1>" + outMessage + "</h1></body></html>";
        } catch (NumberFormatException e){
            response = """
                    HTTP/1.1 400 Bad Request\r
                    Content-Type: text/html\r
                    \r
                    <html>
                    <body>
                        <h1>400 - Bad Request</h1>
                        <p>Not valid parameters provided to request</p>
                    </body></html>""";
        }
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
