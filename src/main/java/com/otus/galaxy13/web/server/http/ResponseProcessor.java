package com.otus.galaxy13.web.server.http;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.otus.galaxy13.web.server.http.ddo.Response;
import com.otus.galaxy13.web.server.http.exceptions.HTTPError;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ResponseProcessor {
    private static final String responseHtmlTemplate = """
        HTTP/1.1 %d %s\r
        Content-Type: text/html\r
        \r
        %s
                           \s""";

    private static final String responseJsonTemplate = """
            HTTP/1.1 %d %s
            Content-Type: application/json
            \r
            \r
            %s
            """;

    public static void sendResponse(Response response, OutputStream outputStream, String type) throws IOException {
        String responseFinal;
        if (type.equals("html")){
            responseFinal = String.format(responseHtmlTemplate, response.getStatusCode(), response.getCodeDescription(), response.getBody());
        } else {
            responseFinal= String.format(responseJsonTemplate, response.getStatusCode(), response.getCodeDescription(), response.getBody());
        }
        outputStream.write(responseFinal.getBytes(StandardCharsets.UTF_8));
    }

    public static void responseErr(HTTPError httpError, OutputStream outputStream, String type) throws IOException {
        String errBody;
        if (type.equals("json")){
            Gson gson = new Gson();
            JsonObject responseJson = getJsonObject(httpError);
            errBody = gson.toJson(responseJson);
        } else {
            String htmlBody = """
                    <html>
                    <body>
                    <h1>%d - %s: %s</h1>
                    </body>
                    </html>""";
            errBody = String.format(htmlBody, httpError.getStatusCode(), httpError.getTitle(), httpError.getDetail());
        }
        Response errResponse = new Response(httpError, errBody);
        sendResponse(errResponse, outputStream, type);
    }

    private static JsonObject getJsonObject(HTTPError httpError) {
        JsonObject errJsonObject = new JsonObject();
        errJsonObject.addProperty("status", String.valueOf(httpError.getStatusCode()));

        JsonObject sourceJsonObject = new JsonObject();
        sourceJsonObject.addProperty("pointer", httpError.getPointer());

        errJsonObject.add("source", sourceJsonObject);
        errJsonObject.addProperty("title", httpError.getTitle());
        errJsonObject.addProperty("detail", httpError.getDetail());

        JsonArray errJsonList = new JsonArray();
        errJsonList.add(errJsonObject);

        JsonObject responseJson = new JsonObject();
        responseJson.add("errors", errJsonList);
        return responseJson;
    }
}
