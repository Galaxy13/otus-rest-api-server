package com.otus.galaxy13.web.server.application.processors;

import com.otus.galaxy13.web.server.application.ddo.File;
import com.otus.galaxy13.web.server.http.HTTPResponse;
import com.otus.galaxy13.web.server.http.ddo.HTTPRequest;
import com.otus.galaxy13.web.server.http.ddo.Response;
import com.otus.galaxy13.web.server.http.exceptions.HTTPError;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileRequestProcessor extends Processor{
    @Override
    public Response execute(HTTPRequest httpRequest, String parameter) throws HTTPError {
        try {
            String content = Files.readString(Path.of("static/" + parameter));
            logger.trace("File passed in path found");
            String body = bodyHandler(httpRequest, new File(content), "fileContentTemplate.html");
            logger.trace("Response body created");
            return HTTPResponse.ok(body);
        } catch (IOException e) {
            logger.debug("Requested file not exists");
            throw HTTPResponse.err404(httpRequest, "Wrong URI or file not exists");
        }
    }
}
