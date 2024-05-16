package com.otus.galaxy13.web.server;

import com.otus.galaxy13.web.server.application.exceptions.HTTPError;
import com.otus.galaxy13.web.server.application.processors.Processor;
import com.otus.galaxy13.web.server.application.processors.UknownOperationProcessor;

public class RouterInstance {
    private Processor pathProcessor;
    private final String routeKey;
    private Class<?> parametrizedLevelPathType;
    private Processor parameterizedLevelPathProcessor;

    public RouterInstance(String currentLevelPath){
        this.pathProcessor = new UknownOperationProcessor();
        this.routeKey = currentLevelPath;
    }

    public RouterInstance(String currentLevelPath, Processor pathProcessor) {
        this.pathProcessor = pathProcessor;
        this.routeKey = currentLevelPath;
    }

    public RouterInstance(String currentLevelPath, Processor pathProcessor, Class<?> parametrizedLevelPathType) {
        this.pathProcessor = pathProcessor;
        this.routeKey = currentLevelPath;
        this.parametrizedLevelPathType = parametrizedLevelPathType;
    }

    public String getChildPath(String input) {
        int indexOfLastSlash = input.lastIndexOf("/");
        return input.substring(indexOfLastSlash);
    }


    public void addParametrizedProcessor(Class<?> cls, Processor pathProcessor){
        parametrizedLevelPathType = cls;
        parameterizedLevelPathProcessor = pathProcessor;
    }

    public Processor getPathProcessor(){
        return pathProcessor;
    }

    public Processor getParameterizedProcessor(){
        return parameterizedLevelPathProcessor;
    }
}
