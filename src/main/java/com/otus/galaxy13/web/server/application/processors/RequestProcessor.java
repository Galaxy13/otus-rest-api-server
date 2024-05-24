package com.otus.galaxy13.web.server.application.processors;

import com.otus.galaxy13.web.server.http.ddo.HTTPRequest;
import com.otus.galaxy13.web.server.http.ddo.Response;
import com.otus.galaxy13.web.server.http.exceptions.HTTPError;

public interface RequestProcessor {
    Response execute(HTTPRequest httpRequest) throws HTTPError, ClassNotFoundException;

    Response execute(HTTPRequest httpRequest, String parameter) throws HTTPError, ClassNotFoundException;
}
