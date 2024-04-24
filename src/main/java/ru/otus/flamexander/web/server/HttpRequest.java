package ru.otus.flamexander.web.server;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest {
    private final String rawRequest;
    private String uri;
    private HttpMethod method;
    private Map<String, String> parameters;

    public String getUri() {
        return uri;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public HttpRequest(String rawRequest) throws BrokenHTTPRequestException {
        this.rawRequest = rawRequest;
        this.parseRequestLine();
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
        Matcher parameterMatcher = Pattern.compile("\\?(\\w+)=(\\d+)").matcher(rawRequest);
        while (parameterMatcher.find()){
            String key = parameterMatcher.group(1);
            String value = parameterMatcher.group(2);
            parameters.put(key, value);
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
