package ru.otus.flamexander.web.server.application.processors;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ResponseProcessor {
    private static final String responseHtmlTemplate = """
        HTTP/1.1 %d %s\r
        Content-Type: text/html\r
        \r
        <html>
        <body>
        %s
        </body></html>""";

    private static final String responseJsonTemplate = """
            HTTP/1.1 %d %s
            Content-Type: application/json
            \r
            \r
            %s
            """;

    public static void responseHtml(int statusCode, String codeDescription, String body, OutputStream outputStream) throws IOException {
        String response = String.format(responseHtmlTemplate, statusCode, codeDescription, body);
        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
    }

    public static void responseJson(int statusCode, String codeDescription, String jsonBody, OutputStream outputStream) throws IOException {
        String response = String.format(responseJsonTemplate, statusCode, codeDescription, jsonBody);
        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
    }

    public static void responseErrJson(Gson gson, int statusCode, String pointer, String title, String detail, OutputStream outputStream) throws IOException {
        JsonObject errJsonObject = new JsonObject();
        errJsonObject.addProperty("status", String.valueOf(statusCode));

        JsonObject sourceJsonObject = new JsonObject();
        sourceJsonObject.addProperty("pointer", pointer);

        errJsonObject.add("source", sourceJsonObject);
        errJsonObject.addProperty("title", title);
        errJsonObject.addProperty("detail", detail);

        JsonArray errJsonList = new JsonArray();
        errJsonList.add(errJsonObject);

        JsonObject responseJson = new JsonObject();
        responseJson.add("errors", errJsonList);
        responseJson(statusCode, title, gson.toJson(responseJson), outputStream);
    }
}
