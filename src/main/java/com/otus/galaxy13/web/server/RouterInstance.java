package com.otus.galaxy13.web.server;

import com.otus.galaxy13.web.server.application.exceptions.HTTPError;
import com.otus.galaxy13.web.server.application.processors.Processor;

public class RouterInstance {
    Processor pathProcessor;
    String routeKey;
    Class<?> parametrizedLevelPathType;
    Processor parameterizedLevelPathProcessor;

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

    public Processor getPathProcessor(String uri) throws HTTPError {
        String[] uriParts = uri.split("/");
        if (uriParts.length > 1){
            if (routerChildPathMap.containsKey(uriParts[1])){
                return routerChildPathMap.get(uriParts[1]).getPathProcessor(getChildPath(uri));
            } else if (uriParts.length == 2) {
                if (parametrizedLevelPathType != null){
                    try {
                        parametrizedLevelPathType.cast(uriParts[1]);
                    } catch (ClassCastException e) {
                        throw new HTTPError(404, uri, "Not Found", "URL not found");
                    }
                } else {
                    throw new HTTPError(404, uri, "Not Found", "URL not found");
                }
            }
        }
        return pathProcessor;
    }


    public void addNewParameterLevel(Class<?> cls, Processor pathProcessor){
        parametrizedLevelPathType = cls;
        parameterizedLevelPathProcessor = pathProcessor;
    }
}
