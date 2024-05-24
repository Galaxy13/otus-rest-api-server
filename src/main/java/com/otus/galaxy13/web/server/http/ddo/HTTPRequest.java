package com.otus.galaxy13.web.server.http.ddo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.otus.galaxy13.web.server.http.HTTPServer;
import com.otus.galaxy13.web.server.http.exceptions.BrokenHTTPRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HTTPRequest {
    private final Logger logger = LoggerFactory.getLogger(HTTPServer.class);
    private final String rawRequest;
    private String uri;
    private final Map<String, String> parameters = new HashMap<>();
    private HTTPMethod method;
    private JsonObject jsonData;
    private String requestType;

    public String getUri() {
        return uri;
    }

    public HTTPRequest(String rawRequest) throws BrokenHTTPRequestException {
        this.rawRequest = rawRequest;
        parseRequestLine();
        if (method != HTTPMethod.GET) {
            parseJsonObject();
        }
        logger.debug("HTTP Request parsed successfully");
    }

    public String getRouteKey() {
        logger.trace("Route key requested");
        return String.format("%s %s", method, uri);
    }

    public JsonObject getBody() {
        logger.trace("Json body requested");
        return jsonData;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getMethod(){
        return method.name();
    }

    public void parseRequestLine() throws BrokenHTTPRequestException {
        Matcher httpMatcher = Pattern.compile("(GET|PUT|POST|DELETE)\\s(/\\S*)").matcher(rawRequest);
        if (httpMatcher.find()){
            method = HTTPMethod.valueOf(httpMatcher.group(1));
            uri = httpMatcher.group(2);
            if (uri.substring(uri.length() - 1).equals("/") && uri.length() > 1){
                uri = uri.substring(0, uri.length() - 1);
            }
            Matcher parameterMatcher = Pattern.compile("[?|&](\\w+)=(\\w+)").matcher(rawRequest);
            while (parameterMatcher.find()) {
                String key = parameterMatcher.group(1);
                String value = parameterMatcher.group(2);
                parameters.put(key, value);
            }
            if (!parameters.isEmpty()) {
                uri = uri.substring(0, uri.indexOf('?'));
            }
        } else {
            logger.warn("Broken HTTP request passed");
            throw new BrokenHTTPRequestException();
        }
        logger.debug(String.format("Successfully parsed 'method': %s and 'uri':%s from raw request", method, uri));
        if (rawRequest.contains("html")){
            requestType = "html";
            logger.trace("HTTP request type set to HTML");
        } else if (rawRequest.contains("json")){
            requestType = "json";
            logger.trace("HTTP request type set to JSON");
        } else {
            logger.warn("HTTP request type not set or unsupported");
            throw new BrokenHTTPRequestException();
        }
    }

    public void parseJsonObject() throws BrokenHTTPRequestException {
        logger.trace("Json object parsing started");
        String[] requestParts = rawRequest.split("\r\n\r\n");
        if (requestParts.length > 1) {
            String jsonString = requestParts[1];
            try {
                jsonData = JsonParser.parseString(jsonString).getAsJsonObject();
            } catch (JsonSyntaxException e) {
                logger.warn("Bad JSON provided with request");
                throw new BrokenHTTPRequestException();
            }
        } else {
            jsonData = new JsonObject();
        }
    }

    public void info() {
        logger.trace(rawRequest);
        logger.debug("URI: " + uri);
        logger.debug("HTTP-method: " + method);
        logger.debug("Parameters: " + parameters + "\n");
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
