package com.otus.galaxy13.web.server.application.processors;

import com.otus.galaxy13.web.server.HttpRequest;
import com.otus.galaxy13.web.server.application.exceptions.HTTPError;
import com.otus.galaxy13.web.server.application.responses.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileRequestProcessor extends Processor{
//    @Override
//    public Response execute(HttpRequest httpRequest, OutputStream output) throws IOException, HTTPError {
//        try {
//            Path filepath = Paths.get("static/" + httpRequest.getParameter("filename"));
//            Files.copy(filepath, output);
//            logger.debug("%s successfully sent to client");
//        } catch (IOException e){
//            logger.debug("Requested file not exists");
//            throw new HTTPError(404, httpRequest.getUri(), "Not found", "Such file not exists");
//        }
//        return new Response(200, "File transfer successful", httpRequest.getRequestType());
//    }
}
