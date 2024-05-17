package com.otus.galaxy13.web.server;

import com.otus.galaxy13.web.server.application.exceptions.HTTPError;
import com.otus.galaxy13.web.server.application.exceptions.WrongParameterException;
import com.otus.galaxy13.web.server.application.processors.Processor;
import com.otus.galaxy13.web.server.application.responses.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router {
    Map<String, Map<String, RouterInstance>> mainRouteMap = new HashMap<>(){{
        put("GET", new HashMap<>());
        put("POST", new HashMap<>());
        put("PUT", new HashMap<>());
        put("DELETE", new HashMap<>());
    }};

    public void putRouterInstance(String requestType, String pathUri, Processor processor){
        Matcher matcher = Pattern.compile("(?<=/\\{)\\S+(?=}$)").matcher(requestType);
        RouterInstance instance;
        if (matcher.find()){
            String parameter = matcher.group(1);
            pathUri = pathUri.substring(0, pathUri.trim().lastIndexOf("/"));
            if (mainRouteMap.get(requestType).containsKey(pathUri)){
                RouterInstance pathRouter = mainRouteMap.get(requestType).get(pathUri);
                pathRouter.addParametrizedProcessor(parameter, processor);
                return;
            } else {
                instance = new RouterInstance(pathUri);
                instance.addParametrizedProcessor(parameter, processor);
            }
        }
        else {
            instance = new RouterInstance(pathUri, processor);
        }
        mainRouteMap.get(requestType).put(pathUri, instance);
    }

    public void get(String pathUri, Processor requestProcessor){
        putRouterInstance("GET", pathUri, requestProcessor);
    }

    public void post(String pathUri, Processor requestProcessor){
        putRouterInstance("POST", pathUri, requestProcessor);
    }

    public void put(String pathUri, Processor requestProcessor){
        putRouterInstance("PUT", pathUri, requestProcessor);
    }

    public void delete(String pathUri, Processor requestProcessor){
        putRouterInstance("DELETE", pathUri, requestProcessor);
    }

    public Response parseRequest(HttpRequest request) throws HTTPError {
        Map<String, RouterInstance> instanceMap = mainRouteMap.get(request.getUri());
        if (instanceMap.containsKey(request.getUri())){
            return instanceMap.get(request.getUri()).getPathProcessor().execute(request);
        } else {
            int indexOfLastSlash = request.getUri().lastIndexOf("/");
            String strippedUri = request.getUri().substring(0, indexOfLastSlash);
            String parameter = request.getUri().substring(indexOfLastSlash + 1);
            if (instanceMap.containsKey(strippedUri)){
                RouterInstance routerInstance = instanceMap.get(strippedUri);
                return routerInstance.getParameterizedProcessor().execute(request, parameter);
            } else {
                throw new HTTPError(404, request.getUri(), "Not Found", "Operaion not found, obsolete or not implemented");
            }
        }
    }
}
