package ru.otus.flamexander.web.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HttpRequest {
    private final String rawRequest;
    private String uri;
    private HttpMethod method;
    private Map<String, String> parameters;
    private JsonObject jsonData;

    public String getUri() {
        return uri;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public HttpRequest(String rawRequest) throws BrokenHTTPRequestException {
        this.rawRequest = rawRequest;
        parseRequestLine();
        parseJsonObject();
    }

    public String getRouteKey() {
        return String.format("%s %s", method, uri);
    }

    public JsonObject getBody() {
        return jsonData;
    }

    public void parseRequestLine() throws BrokenHTTPRequestException {
        Matcher httpMatcher = Pattern.compile("(GET|PUT|POST|DELETE)\\s(/\\w*)").matcher(rawRequest);
        if (httpMatcher.find()){
            this.method = HttpMethod.valueOf(httpMatcher.group(1));
            uri = httpMatcher.group(2);
        } else {
            throw new BrokenHTTPRequestException();
        }
        this.parameters = new HashMap<>();
        Matcher parameterMatcher = Pattern.compile("[?|&](\\w+)=(\\w+)").matcher(rawRequest);
        while (parameterMatcher.find()){
            String key = parameterMatcher.group(1);
            String value = parameterMatcher.group(2);
            parameters.put(key, value);
        }
    }

    public void parseJsonObject() {
        String[] requestParts = rawRequest.split("\r\n\r\n");
        if (requestParts.length > 1) {
            String jsonString = requestParts[1];
            jsonData = JsonParser.parseString(jsonString).getAsJsonObject();
        }
    }

    public void info(int debugDepth) {
        if (debugDepth > 0) {
            System.out.println(rawRequest);
        }
        if (debugDepth > 1){
            System.out.println("URI: " + uri);
            System.out.println("HTTP-method: " + method);
            System.out.println("Parameters: " + parameters + "\n");
        }
    }
}
