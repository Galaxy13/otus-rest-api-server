package com.otus.galaxy13.web.server.application.processors;

import com.otus.galaxy13.web.server.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Processor implements RequestProcessor {
    final Logger logger = LoggerFactory.getLogger(Processor.class);

    int idRetriever(HttpRequest request) throws NumberFormatException{
        String[] splitUri = request.getUri().split("/");
        if (splitUri.length == 2){
            return Integer.parseInt(splitUri[1]);
        } else {
            return -1;
        }
    }
}
