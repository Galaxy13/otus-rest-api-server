package ru.otus.flamexander.web.server.processors;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ResponseProcessor {
    private static final String responseTemplate = """
        HTTP/1.1 %d %s\r
        Content-Type: text/html\r
        \r
        <html>
        <body>
        %s
        </body></html>""";
    public static void responseWrite(int statusCode, String codeDescription, String body, OutputStream outputStream) throws IOException {
        String response = String.format(responseTemplate, statusCode, codeDescription, body);
        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
