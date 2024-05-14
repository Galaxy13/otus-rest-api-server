package com.otus.galaxy13.web.server.application.processors;

import com.otus.galaxy13.web.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

public class CalculatorRequestProcessor extends Processor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        super.logger.trace(CalculatorRequestProcessor.class.getName() + " executed");
        try {
            int a = Integer.parseInt(httpRequest.getParameter("a"));
            int b = Integer.parseInt(httpRequest.getParameter("b"));
            int result = a + b;
            String outMessage = a + " + " + b + " = " + result;
            ResponseProcessor.responseHtml(200, "OK", "<h1>" + outMessage + "</h1>", output);
            super.logger.debug(CalculatorRequestProcessor.class + " successfully respond");
        } catch (NumberFormatException e){
            ResponseProcessor.responseHtml(400, "Bad Request", "<h1>400 - Bad Request</h1>" +
                        "<p>Not valid parameters provided to request</p>", output);
            super.logger.debug("Not valid parameters passed to CalculatorProcessor. " + e.getMessage());
        }
    }
}
