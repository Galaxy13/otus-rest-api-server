package com.otus.galaxy13.web.server;

import com.otus.galaxy13.web.server.application.processors.Processor;
import com.otus.galaxy13.web.server.application.processors.UknownOperationProcessor;

public class RouterInstance {
    private final Processor pathProcessor;
    private final String routeKey;
    private String parameterName;
    private Processor parameterizedLevelPathProcessor;

    public RouterInstance(String currentLevelPath){
        this.pathProcessor = new UknownOperationProcessor();
        this.routeKey = currentLevelPath;
    }

    public RouterInstance(String currentLevelPath, Processor pathProcessor) {
        this.pathProcessor = pathProcessor;
        this.routeKey = currentLevelPath;
    }

    public String getChildPath(String input) {
        int indexOfLastSlash = input.lastIndexOf("/");
        return input.substring(indexOfLastSlash);
    }

    public void addParametrizedProcessor(String parameterName, Processor pathProcessor){
        this.parameterName = parameterName;
        parameterizedLevelPathProcessor = pathProcessor;
    }

    public Processor getPathProcessor(){
        return pathProcessor;
    }

    public Processor getParameterizedProcessor(){
        return parameterizedLevelPathProcessor;
    }

}
