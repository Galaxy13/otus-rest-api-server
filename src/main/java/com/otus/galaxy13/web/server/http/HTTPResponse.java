package com.otus.galaxy13.web.server.http;

import com.otus.galaxy13.web.server.http.ddo.HTTPRequest;
import com.otus.galaxy13.web.server.http.ddo.Response;
import com.otus.galaxy13.web.server.http.exceptions.HTTPError;

public class HTTPResponse {
    public static Response ok(String body) {
        return new Response(200, "OK", body);
    }

    public static HTTPError err404(HTTPRequest request, String detail) {
        return new HTTPError(404, request.getUri(), "Not found", detail);
    }

    public static HTTPError err400(HTTPRequest request, String detail) {
        return new HTTPError(400, request.getUri(), "Bad Request", detail);
    }

    public static HTTPError err500(HTTPRequest request, String detail) {
        return new HTTPError(500, request.getUri(), "Internal Error", detail);
    }
}
