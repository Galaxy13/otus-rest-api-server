package com.otus.galaxy13.web.server.http.routing;

import com.otus.galaxy13.web.server.application.processors.Processor;
import com.otus.galaxy13.web.server.application.processors.UknownOperationProcessor;

public class RouterInstance {
    private final Processor pathProcessor;
    private Processor parameterizedLevelPathProcessor;

    public RouterInstance() {
        this.pathProcessor = new UknownOperationProcessor();
    }

    public RouterInstance(Processor pathProcessor) {
        this.pathProcessor = pathProcessor;
    }

    public void addParametrizedProcessor(Processor pathProcessor) {
        parameterizedLevelPathProcessor = pathProcessor;
    }

    public Processor getPathProcessor(){
        return pathProcessor;
    }

    public Processor getParameterizedProcessor(){
        return parameterizedLevelPathProcessor;
    }

}
